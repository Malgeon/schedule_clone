package com.example.schedule_clone.domain.userevent

import androidx.annotation.WorkerThread
import com.example.schedule_clone.data.pref.UserEventMessage
import com.example.schedule_clone.data.session.SessionRepository
import com.example.schedule_clone.domain.sessions.LoadUserSessionUseCaseResult
import com.example.schedule_clone.domain.users.ReservationRequestAction
import com.example.schedule_clone.domain.users.ReservationRequestAction.RequestAction
import com.example.schedule_clone.domain.users.ReservationRequestAction.SwapAction
import com.example.schedule_clone.domain.users.StarUpdatedStatus
import com.example.schedule_clone.domain.users.SwapRequestAction
import com.example.schedule_clone.domain.users.SwapRequestParameters
import com.example.schedule_clone.model.ConferenceDay
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.userdata.UserEvent
import com.example.schedule_clone.model.userdata.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class DefaultSessionAndUserEventRepository @Inject constructor(
    private val userEventDataSource: UserEventDataSource,
    private val sessionRepository: SessionRepository
) : SessionAndUserEventRepository {

    @WorkerThread
    override fun getObservableUserEvents(
        userId: String?
    ): Flow<Result<ObservableUserEvent>> {
        return flow {
            emit(Result.Loading)
            // If there no logged-in user, return the map with null UserEvents
            if (userId == null) {
                Timber.d(
                    """EventRepository: No user logged in,
                        |returning session without user events.""".trimMargin()
                )
                val allSessions = sessionRepository.getSessions()
                val userSessions = mergeUserDataAndSessions(null, allSessions)
                emit(
                    Result.Success(
                        ObservableUserEvent(
                            userSessions = userSessions
                        )
                    )
                )
            } else {
                emitAll(
                    userEventDataSource.getObservableUserEvents(userId).map { userEvents ->
                        Timber.d(
                            """EventRepository: Received ${userEvents.userEvents.size}
                                |user events changes""".trimMargin()
                        )
                        // Get the sessions, synchronously
                        val allSessions = sessionRepository.getSessions()
                        val userSessions = mergeUserDataAndSessions(userEvents, allSessions)
                        // TODO(b/122306429) expose user events messages separately
                        val userEventsMessageSession = allSessions.firstOrNull {
                            it.id == userEvents.userEventsMessage?.sessionId
                        }
                        Result.Success(
                            ObservableUserEvent(
                                userSessions = userSessions,
                                userMessage = userEvents.userEventsMessage,
                                userMessageSession = userEventsMessageSession
                            )
                        )
                    }
                )
            }
        }
    }

    override fun getObservableUserEvent(
        userId: String?,
        eventId: SessionId
    ): Flow<Result<LoadUserSessionUseCaseResult>> {
        // If there is no logged-in user, return the session with a null UserEvent
        if (userId == null) {
            Timber.d("EventRepository: No user logged in, returning session without user event")
            val session = sessionRepository.getSession(eventId)
            return flow {
                emit(
                    Result.Success(
                        LoadUserSessionUseCaseResult(
                            userSession = UserSession(session, createDefaultUserEvent(session))
                        )
                    )
                )
            }
        }

        // Observes the user events and merges them with session data.
        return userEventDataSource.getObservableUserEvent(userId, eventId).map { userEventResult ->
            Timber.d("EventRepository: Received user event changes")
            // Get the session, synchronously
            val event = sessionRepository.getSession(eventId)

            // Merges session with user data and emits the result
            val userSession = UserSession(
                event,
                userEventResult.userEvent ?: createDefaultUserEvent(event)
            )

            Result.Success(LoadUserSessionUseCaseResult(userSession = userSession))
        }
    }

    override fun getUserEvents(userId: String?): List<UserEvent> {
        return userEventDataSource.getUserEvents(userId ?: "")
    }

    override fun getUserSession(userId: String, sessionId: SessionId): UserSession {
        val session = sessionRepository.getSession(sessionId)
        val userEvent = userEventDataSource.getUserEvent(userId, sessionId)
            ?: throw Exception("UserEvent not found")

        return UserSession(
            session = session,
            userEvent = userEvent
        )
    }

    override suspend fun starEvent(
        userId: String,
        userEvent: UserEvent
    ): Result<StarUpdatedStatus> = userEventDataSource.startEvent(userId, userEvent)

    override suspend fun recordFeedbackSent(userId: String, userEvent: UserEvent): Result<Unit> {
        return userEventDataSource.recordFeedbackSent(userId, userEvent)
    }

    override suspend fun changeReservation(
        userId: String,
        sessionId: SessionId,
        action: ReservationRequestAction
    ): Result<ReservationRequestAction> {
        val sessions = sessionRepository.getSessions().associateBy { it.id }
        val userEvents = getUserEvents(userId)
        val session = sessionRepository.getSession(sessionId)
        val overlappingId = findOverlappingReservationId(session, action, sessions, userEvents)
        if (overlappingId != null) {
            // If there is already an overlapping reservation, return the result as
            // SwapAction is needed.
            val overlappingSession = sessionRepository.getSession(overlappingId)
            Timber.d(
                """User is trying to reserve a session that overlaps with the
                    |session id: $overlappingId, title: ${overlappingSession.title}""".trimMargin()
            )
            return Result.Success(
                SwapAction(
                    SwapRequestParameters(
                        userId,
                        fromId = overlappingId,
                        fromTitle = overlappingSession.title,
                        toId = sessionId,
                        toTitle = session.title
                    )
                )
            )
        }
        return userEventDataSource.requestReservation(userId, session, action)
    }

    override suspend fun swapReservation(
        userId: String,
        fromId: SessionId,
        toId: SessionId
    ): Result<SwapRequestAction> {
        val toSession = sessionRepository.getSession(toId)
        val fromSession = sessionRepository.getSession(fromId)
        return userEventDataSource.swapReservation(userId, fromSession, toSession)
    }

    private fun findOverlappingReservationId(
        session: Session,
        action: ReservationRequestAction,
        sessions: Map<String, Session>,
        userEvents: List<UserEvent>
    ): String? {
        if (action !is RequestAction) return null
        val overlappingUserEvent = userEvents.find {
            sessions[it.id]?.isOverlapping(session) == true &&
                    (it.isReserved() || it.isWaitlisted())
        }
        return overlappingUserEvent?.id
    }

    private fun createDefaultUserEvent(session: Session): UserEvent {
        return UserEvent(id = session.id)
    }

    /**
     * Merges user data with sessions
     */
    @WorkerThread
    private fun mergeUserDataAndSessions(
        userData: UserEventsResult?,
        allSessions: List<Session>
    ): List<UserSession> {
        // If there is no logged-in user, return the map with null UserEvents
        if (userData == null) {
            return allSessions.map { UserSession(it, createDefaultUserEvent(it)) }
        }

        val (userEvents, _) = userData
        val eventIdToUserEvent = userEvents.associateBy { it.id }
        return allSessions.map {
            UserSession(it, eventIdToUserEvent[it.id] ?: createDefaultUserEvent(it))
        }
    }

    override fun getConferenceDays(): List<ConferenceDay> = sessionRepository.getConferenceDays()
}

interface SessionAndUserEventRepository {

    // TODO(b/122112739): Repository should not have source dependency on UseCase result
    fun getObservableUserEvents(
        userId: String?
    ): Flow<Result<ObservableUserEvent>>

    // TODO(b/122112739): Repository should not have source dependency on UseCase result
    fun getObservableUserEvent(
        userId: String?,
        eventId: SessionId
    ): Flow<Result<LoadUserSessionUseCaseResult>>

    fun getUserEvents(userId: String?): List<UserEvent>

    suspend fun changeReservation(
        userId: String,
        sessionId: SessionId,
        action: ReservationRequestAction
    ): Result<ReservationRequestAction>

    suspend fun swapReservation(
        userId: String,
        fromId: SessionId,
        toId: SessionId
    ): Result<SwapRequestAction>

    suspend fun starEvent(userId: String, userEvent: UserEvent): Result<StarUpdatedStatus>

    suspend fun recordFeedbackSent(
        userId: String,
        userEvent: UserEvent
    ): Result<Unit>

    fun getConferenceDays(): List<ConferenceDay>

    fun getUserSession(userId: String, sessionId: SessionId): UserSession
}

data class ObservableUserEvent(
    val userSessions: List<UserSession>,

    /** A message to show to the user with important changes like reservation confirmations */
    val userMessage: UserEventMessage? = null,

    /** The session the user message is about, if any. */
    val userMessageSession: Session? = null
)