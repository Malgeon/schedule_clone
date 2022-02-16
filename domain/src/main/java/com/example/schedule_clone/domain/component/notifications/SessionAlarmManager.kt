package com.example.schedule_clone.domain.component.notifications

import android.app.AlarmManager
import android.app.AlarmManager.RTC_WAKEUP
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.getSystemService
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.util.toEpochMilli
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

        val upcomingIntent =
            makePendingIntent(session.id, AlarmBroadcastReceiver.CHANNEL_ID_UPCOMING)
        upcomingIntent?.let {
            scheduleAlarmForPreSession(it, session)
        }
        if (userSession.isPostSessionNotificationRequired()) {
            val feedbackIntent =
                makePendingIntent(session.id, AlarmBroadcastReceiver.CHANNEL_ID_FEEDBACK)
            feedbackIntent?.let {
                scheduleAlarmForPostSession(it, session)
            }
        }

    }

    open fun cancelAlarmForSession(sessionId: SessionId) {

    }

    private fun makePendingIntent(sessionId: SessionId, channel: String): PendingIntent? {
        return PendingIntent.getBroadcast(
            context,
            // To make the requestCode unique for the upcoming and feedback channels for the
            // same session, concatenating the strings
            (sessionId + channel).hashCode(),
            Intent(context, Alarm)
        )
    }

    private fun scheduleAlarmForPreSession(pendingIntent: PendingIntent, session: Session) {
        val triggerAtMillis = session.startTime.toEpochMilli() - alarmTimeDelta
        scheduleAlarmFor(
            pendingIntent, session, triggerAtMillis,
            AlarmBroadcastReceiver.CHANNEL_ID_UPCOMING
        )
    }

    private fun scheduleAlarmForPostSession(pendingIntent: PendingIntent, session: Session) {
        val triggerAtMillis = session.endTime.toEpochMilli() + alarmTimeDelta
        scheduleAlarmFor(
            pendingIntent, session, triggerAtMillis,
            AlarmBroadcastReceiver.CHANNEL_ID_FEEDBACK
        )
    }

    private fun scheduleAlarmFor(
        pendingIntent: PendingIntent,
        session: Session,
        triggerAtMillis: Long,
        channel: String
    ) {
        systemAlarmManager?.let {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                systemAlarmManager,
                RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
            Timber.d(
              """Schedule aram for session ${session.title} at $triggerAtMillis
                  |for channel: $channel""".trimMargin()
            )
        }
    }

    companion object {
        private val alarmTimeDelta = TimeUnit.MINUTES.toMillis(5)
    }
}