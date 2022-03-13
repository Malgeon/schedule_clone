package com.example.schedule_clone.domain.search

import com.example.schedule_clone.domain.FlowUseCase
import com.example.schedule_clone.domain.userevent.SessionAndUserEventRepository
import com.example.schedule_clone.model.filters.Filter
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class SessionSearchUseCaseParams(
    val userId: String?,
    val query: String,
    val filters: List<Filter>
)

class SessionSearchUseCase @Inject constructor(
    private val repository: SessionAndUserEventRepository,
    private val textMatchStrategy: SessionTextMatchStrategy,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<SessionSearchUseCaseParams, List<UserSession>>(dispatcher) {

    override fun execute(parameters: SessionSearchUseCaseParams): Flow<Result<List<UserSession>>> {

        TODO("Not yet implemented")
    }
}