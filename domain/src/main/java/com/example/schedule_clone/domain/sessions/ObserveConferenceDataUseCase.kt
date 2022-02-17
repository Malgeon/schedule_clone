package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.data.ConferenceDataRepository
import com.example.schedule_clone.domain.FlowUseCase
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Forces a refresh in the conference data repository.
 */
open class ObserveConferenceDataUseCase @Inject constructor(
    private val repository: ConferenceDataRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Long>(dispatcher){
    override fun execute(parameters: Unit): Flow<Result<Long>> =
        repository.dataLastUpDatedObservable.map {
            Result.Success(it)
        }
}