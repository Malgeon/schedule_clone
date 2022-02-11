package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.data.pref.UserEventMessage
import com.example.schedule_clone.model.userdata.UserSession

class LoadUserSessionUseCase {
}


data class LoadUserSessionUseCaseResult(
    val userSession: UserSession,

    /** A message to show to the user with important changes like reservation confirmations */
    val userMessage: UserEventMessage? = null
)