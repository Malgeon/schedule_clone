package com.example.schedule_clone.domain.userevent

import androidx.annotation.WorkerThread
import com.example.schedule_clone.data.pref.UserEventMessage
import com.example.schedule_clone.data.session.SessionRepository
import com.example.schedule_clone.domain.sessions.LoadUserSessionUseCaseResult
import com.example.schedule_clone.domain.users.ReservationRequestAction
import com.example.schedule_clone.domain.users.SwapRequestAction
import com.example.schedule_clone.domain.users.UserEventDataSource
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.userdata.UserEvent
import com.example.schedule_clone.model.userdata.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
        TODO("Not yet implemented")
    }

    override fun getObservableUserEvent(
        userId: String?,
        eventId: SessionId
    ): Flow<Result<LoadUserSessionUseCaseResult>> {
        TODO("Not yet implemented")
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