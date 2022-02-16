package com.example.schedule_clone.domain.prefs

import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Returns whether the schedule UI hints have been shown.
 */
class ScheduleUiHintsShownUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, Boolean>(dispatcher) {
    override suspend fun execute(parameters: Unit): Boolean =
        preferenceStorage.areScheduleUiHintsShown()

}