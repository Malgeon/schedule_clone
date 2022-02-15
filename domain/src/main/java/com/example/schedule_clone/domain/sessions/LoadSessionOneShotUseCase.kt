package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.data.session.SessionRepository
import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

open class LoadSessionOneShotUseCase @Inject constructor(
    private val repository: SessionRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<SessionId, Session>(dispatcher){

    override suspend fun execute(parameters: SessionId): Session {
        return repository.getSession(parameters)
    }
}