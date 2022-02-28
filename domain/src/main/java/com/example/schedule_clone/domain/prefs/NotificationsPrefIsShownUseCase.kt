package com.example.schedule_clone.domain.prefs

import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Returns whether the notifications preference has been shown to the user.
 */
open class NotificationsPrefIsShownUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, Boolean>(dispatcher) {
    // TODO use as flow
    override suspend fun execute(parameters: Unit): Boolean =
        preferenceStorage.notificationsPreferenceShown.first()
}