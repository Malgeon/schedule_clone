package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.data.pref.UserEventMessage
import com.example.schedule_clone.domain.FlowUseCase
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.ZonedDateTime
import javax.inject.Inject

open class LoadScheduleUserSessionsUseCase @Inject constructor(

    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<LoadScheduleUserSessionsParameters, LoadScheduleUserSessionsResult> {
    override fun execute(parameters: LoadScheduleUserSessionsParameters): Flow<Result<LoadScheduleUserSessionsResult>> {
        TODO("Not yet implemented")
    }
}

data class LoadScheduleUserSessionsParameters(
    val userId: String?,
    val now: ZonedDateTime = ZonedDateTime.now()
)

data class LoadScheduleUserSessionsResult(
    val userSessions: List<UserSession>,

    /** A message to show to the user with important changes like reservation confirmations */
    val userMessage: UserEventMessage? = null,

    /** The session the user message is about, if any. */
    val userMessageSession: Session? = null,

    /** The total number of sessions. */
    val userSessionCount: Int = userSessions.size,

    /** The location of the first session which has not finished. */
    val firstUnfinishedSessionIndex: Int = -1,

    val dayIndexer: ConferenceDayIndexer
)