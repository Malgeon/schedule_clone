package com.example.schedule_clone.domain.users

import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.domain.sessions.StarReserveNotificationAlarmUpdater
import com.example.schedule_clone.domain.userevent.SessionAndUserEventRepository
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class StarEventAndNotifyUseCase @Inject constructor(
    private val repository: SessionAndUserEventRepository,
    private val alarmUpdater: StarReserveNotificationAlarmUpdater,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<StarEventParameter, StarUpdatedStatus>(ioDispatcher) {

    override suspend fun execute(parameters: StarEventParameter): StarUpdatedStatus {
        TODO("Not yet implemented")
    }
}

data class StarEventParameter(
    val userId: String,
    val userSession: UserSession
)

enum class StarUpdatedStatus {
    STARRED,
    UNSTARRED
}
