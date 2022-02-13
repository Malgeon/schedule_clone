package com.example.schedule_clone.domain.sessions

import com.example.schedule_clone.domain.component.notifications.SessionAlarmManager
import com.example.schedule_clone.domain.userevent.SessionAndUserEventRepository
import com.example.schedule_clone.shared.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
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
){
}