package com.example.schedule_clone.domain.component.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.shared.di.ApplicationScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

/**
 * Receives broadcast intents with information for session notifications.
 */
@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferencesStorage: PreferenceStorage

    @Inject
    lateinit var loadUserSession: LoadUserSessionOneShatUserCase

    @Inject
    lateinit var loadSession: LoadSessionOneShotUseCase

    @Inject
    lateinit var alarmManager: SessionAlarmManager

    @Inject
    lateinit var authIdDataSource: AuthIdDataSource

    @ApplicationScope
    @Inject
    lateinit var externalScope: CoroutineScope



    override fun onReceive(p0: Context?, p1: Intent?) {
        TODO("Not yet implemented")
    }


}