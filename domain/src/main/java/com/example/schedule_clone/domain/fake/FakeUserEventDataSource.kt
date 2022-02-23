package com.example.schedule_clone.domain.fake

import com.example.schedule_clone.data.BootstrapConferenceDataSource
import com.example.schedule_clone.domain.userevent.UserEventDataSource
import com.example.schedule_clone.domain.userevent.UserEventResult
import com.example.schedule_clone.domain.userevent.UserEventsResult
import com.example.schedule_clone.domain.users.StarUpdatedStatus
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.reservations.ReservationRequestResult
import com.example.schedule_clone.model.reservations.ReservationRequestResult.ReservationRequestStatus.RESERVE_SUCCEEDED
import com.example.schedule_clone.model.userdata.UserEvent
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.shared.result.Result.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Returns data loaded from a local JSON file for development and testing.
 */
object FakeUserEventDataSource : UserEventDataSource {

    private val conferenceData = BootstrapConferenceDataSource.getOfflineConferenceData()!!
    private val userEvents = ArrayList<UserEvent>()

    init {
        conferenceData.sessions.forEachIndexed { i, session ->
            val reservation = ReservationRequestResult(
                RESERVE_SUCCEEDED, "123",
                System.currentTimeMillis()
            )
            if (i in 1..50) {
                userEvents.add(
                    UserEvent(
                        session.id,
                        isStarred = i % 2 == 0,
                        reservationRequestResult = reservation
                    )
                )
            }
        }
    }

    override fun getObservableUserEvents(userId: String): Flow<UserEventsResult> {
        return flow { emit(UserEventsResult(userEvents)) }
    }

    override fun getObservableUserEvent(
        userId: String,
        eventId: SessionId
    ) = flow {
        emit(UserEventResult(userEvents[0]))
    }

    override suspend fun startEvent(
        userId: String,
        userEvent: UserEvent
    ) = Success(
        if (userEvent.isStarred) StarUpdatedStatus.STARRED
        else StarUpdatedStatus.UNSTARRED
    )

    override suspend fun recordFeedbackSent(
        userId: String,
        userEvent: UserEvent
    ): Result<Unit> = Success(Unit)
}