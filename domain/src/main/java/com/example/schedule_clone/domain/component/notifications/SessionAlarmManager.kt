package com.example.schedule_clone.domain.component.notifications

import android.app.AlarmManager
import android.content.Context
import androidx.core.content.getSystemService
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.userdata.UserSession
import dagger.hilt.android.qualifiers.ApplicationContext
import org.threeten.bp.Instant
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class SessionAlarmManager @Inject constructor(@ApplicationContext val context: Context){

    private val systemAlarmManager: AlarmManager? = context.getSystemService()

    /**
     * Schedules an alarm for a session.
     */
    fun setAlarmForSession(userSession: UserSession) {
        val session = userSession.session
        if ((session.startTime.toInstant().minusMillis(alarmTimeDelta)).isBefore(Instant.now())) {
            Timber.d("Trying to schedule an alarm for a past session, ignoring.")
            return
        }

        cancelAlarmForSession(session.id)

    }

    open fun cancelAlarmForSession(sessionId: SessionId) {

    }

    companion object {
        private val alarmTimeDelta = TimeUnit.MINUTES.toMillis(5)
    }
}