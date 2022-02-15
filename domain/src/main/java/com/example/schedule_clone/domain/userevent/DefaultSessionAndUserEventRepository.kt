package com.example.schedule_clone.domain.userevent

import androidx.annotation.WorkerThread
import com.example.schedule_clone.data.pref.UserEventMessage
import com.example.schedule_clone.data.session.SessionRepository
import com.example.schedule_clone.domain.sessions.LoadUserSessionUseCaseResult
import com.example.schedule_clone.domain.users.ReservationRequestAction
import com.example.schedule_clone.domain.users.SwapRequestAction
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
    override fun getObservableUserEvents(userId: String?): Flow<Result<ObservableUserEvent>> {
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
                        val userEventsMessageSession = allSessions.firstOrNull{
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
        TODO("Not yet implemented")
    }

    override suspend fun changeReservation(
        userId: String,
        sessionId: SessionId,
        action: ReservationRequestAction
    ): Result<ReservationRequestAction> {
        TODO("Not yet implemented")
    }

    override suspend fun swapReservation(
        userId: String,
        fromId: SessionId,
        toId: SessionId
    ): Result<SwapRequestAction> {
        TODO("Not yet implemented")
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
            return allSessions.map { UserSession(it, createDefaultUserEvent(it))}
        }

        val (userEvents, _) = userData
        val eventIdToUserEvent = userEvents.associateBy { it.id }
        return allSessions.map {
            UserSession(it, eventIdToUserEvent[it.id] ?: createDefaultUserEvent(it))
        }
    }
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

}

data class ObservableUserEvent(
    val userSessions: List<UserSession>,

    val userMessage: UserEventMessage? = null,

    val userMessageSession: Session? = null
)