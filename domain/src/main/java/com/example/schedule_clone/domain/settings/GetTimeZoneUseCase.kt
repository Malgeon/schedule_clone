package com.example.schedule_clone.domain.settings

import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTimeZoneUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, Boolean>(dispatcher) {
    override suspend fun execute(parameters: Unit) =
        preferenceStorage.preferConferenceTimeZone.first()
}