package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.domain.component.notifications.SessionAlarmManager
import com.example.schedule_clone.domain.userevent.ObservableUserEvents
import com.example.schedule_clone.domain.userevent.SessionAndUserEventRepository
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.shared.result.data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Sets a notification for each session that is starred or reserved by the user.
 */
@Singleton
class NotificationAlarmUpdater @Inject constructor(
    private val alarmManager: SessionAlarmManager,
    private val repository: SessionAndUserEventRepository,
    @ApplicationScope private val externalScope: CoroutineScope
) {

    fun updateAll(userId: String) {
        externalScope.launch {
            val events = repository.getObservableUserEvents(userId).first() { it is Result.Success }
            events.data?.let { data ->
                processEvents(userId, data)
            }
        }
    }

    private fun processEvents(
        userId: String,
        sessions: ObservableUserEvents
    ) {
        Timber.d("Setting all the alarms for user $userId")
        val startWork = System.currentTimeMillis()
        sessions.userSessions.forEach { session: UserSession ->
            if (session.userEvent.isStarred || session.userEvent.isReserved()) {
                alarmManager.setAlarmForSession(session)
            }
        }
        Timber.d("Work finished in ${System.currentTimeMillis() - startWork} ms")
    }
}


@Singleton
open class StarReserveNotificationAlarmUpdater @Inject constructor(
    private val alarmManager: SessionAlarmManager
) {
    open fun updateSession(
        userSession: UserSession,
        requestNotification: Boolean
    ) {
        if (requestNotification) {
            alarmManager.setAlarmForSession(userSession)
        } else {
            alarmManager.cancelAlarmForSession(userSession.session.id)
        }
    }
}