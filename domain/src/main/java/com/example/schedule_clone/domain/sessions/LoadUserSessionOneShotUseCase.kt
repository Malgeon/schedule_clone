package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.domain.userevent.DefaultSessionAndUserEventRepository
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class LoadUserSessionOneShotUseCase @Inject constructor(
    private val userEventRepository: DefaultSessionAndUserEventRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Pair<String, SessionId>, UserSession>(dispatcher) {

    override suspend fun execute(parameters: Pair<String, SessionId>): UserSession {
        val (userId, eventId) = parameters

        return userEventRepository.getUserSession(userId, eventId)
    }
}