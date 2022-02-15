package com.example.schedule_clone.domain.component.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.WorkerThread
import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.data.signin.datasources.AuthIdDataSource
import com.example.schedule_clone.domain.sessions.LoadSessionOneShotUseCase
import com.example.schedule_clone.domain.sessions.LoadUserSessionOneShotUseCase
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.shared.result.Result.Success
import com.example.schedule_clone.shared.di.ApplicationScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import org.threeten.bp.Instant
import timber.log.Timber
import javax.inject.Inject

/**
 * Receives broadcast intents with information for session notifications.
 */
@AndroidEntryPoint
class AlarmBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferencesStorage: PreferenceStorage

    @Inject
    lateinit var loadUserSession: LoadUserSessionOneShotUseCase

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

    @WorkerThread
    private suspend fun checkThenShowPreSessionNotification(
        context: Context,
        sessionId: String,
        userId: String
    ) {
        // TODO use preferToReceiveNotifications as flow
        if (!preferencesStorage.preferToReceiveNotifications.first()) {
            Timber.d("User disabled notifications, not showing")
            return
        }

        Timber.d("Showing pre session notification for $sessionId, user $userId")

        val userEvent: Result<UserSession>? = getUserEvent(userId, sessionId)
        // Don't notify if for some reason the event is no longer starred or reserved.
        if (userEvent is Success) {
            val event = userEvent.data.userEvent
            if (event.isPreSessionNotificationRequired() &&
                    isSessionCurrent(userEvent.data.session)
            ) {
                try {
                    val notificationId = showPreSessionNotification(context, userEvent.data.session)
                    // Dismiss in any case
                    alarmManager.dismissNotificationInFiveMinutes(notificationId)
                } catch (ex: Exception) {
                    Timber.e(ex)
                    return
                }
            }
        } else {
            // There was no way to get UserEvent info, notify in case of connectivity error.
            notifyWithoutUserEvent(sessionId, context)
        }
    }

    private suspend fun notifyWithoutUserEvent(sessionId: String, context: Context) {
        return try {
            // Using coroutineScope to propagate exception to the try/catch block
            coroutineScope {
                // Using async coroutine builder to wait for the result of the use case computation
                val session = async { loadSession(sessionId) }.await()
                if (session is Success) {
                    val notificationId = showPreSessionNotification(context, session.data)
                    alarmManager.dismissNotificationInFiveMinutes(notificationId)
                } else{
                    Timber.e("Session couldn't be loaded for notification")
                }
            }
        } catch (ex: Exception) {
            Timber.e("Exception loading session for notification: ${ex.message}")
        }
    }

    private suspend fun getUserEvent(userId: String, sessionId: String): Result<UserSession>? {
        return try {
            // Using coroutineScope to propagate exception to the try/catch block
            coroutineScope {
                // Using async coroutine builder to wait for the result of the use case computation
                async { loadUserSession(userId to sessionId) }.await()
            }
        } catch (ex: Exception) {
            Timber.e(
                """Session notification is set, however there was an error confirming
                    |that the event is still starred. Showing notification""".trimMargin()
            )
            null
        }
    }

    @WorkerThread
    private fun showPreSessionNotification(context: Context, session: Session): Int {
        val notificationManager: NotificationManager = context.getSystemService()
            ?: throw Exception("Notification Manager not found.")


    }

    private fun isSessionCurrent(session: Session): Boolean {
        return session.startTime.toInstant().isAfter(Instant.now())
    }

    companion object {
        const val EXTRA_NOTIFICATION_CHANNEL = "notification_channel"
        const val EXTRA_SESSION_ID = "user_event_extra"

        /** If this flag it set to true in session detail, the show rate dialog should be opened */
        const val EXTRA_SHOW_RATE_SESSION_FLAG = "show_rate_session_extra"

        const val QUERY_SESSION_ID = "session_id"
        const val CHANNEL_ID_UPCOMING = "upcoming_channel_id"
        const val CHANNEL_ID_FEEDBACK = "feedback_channel_id"
    }
}