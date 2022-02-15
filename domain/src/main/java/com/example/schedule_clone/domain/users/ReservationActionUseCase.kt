package com.example.schedule_clone.domain.users

import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.domain.sessions.StarReserveNotificationAlarmUpdater
import com.example.schedule_clone.domain.userevent.SessionAndUserEventRepository
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class ReservationActionUseCase @Inject constructor(
    private val repository: SessionAndUserEventRepository,
    private val alarmUpdater: StarReserveNotificationAlarmUpdater,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<ReservationRequestParameters, ReservationRequestAction>(ioDispatcher){

    override suspend fun execute(parameters: ReservationRequestParameters): ReservationRequestAction {
        TODO("Not yet implemented")
    }

}

data class ReservationRequestParameters(
    val userId: String,
    val sessionId: SessionId,
    val action: ReservationRequestAction,
    val userSession: UserSession?
)

sealed class ReservationRequestAction {
    class RequestAction : ReservationRequestAction() {
        override fun equals(other: Any?): Boolean {
            return other is RequestAction
        }

        // This class isn't intended to be used as a key of a collection. Overriding this to remove
        // the lint warning
        @Suppress("redundant")
        override fun hashCode(): Int {
            return super.hashCode()
        }

    }

    class CancelAction : ReservationRequestAction() {
        override fun equals(other: Any?): Boolean {
            return other is CancelAction
        }

        // This class isn't intended to be used as a key of a collection. Overriding this to remove
        // the lint warning
        @Suppress("redundant")
        override fun hashCode(): Int {
            return super.hashCode()
        }
    }

    class SwapAction(val parameters: SwapRequestParameters) : ReservationRequestAction()
}