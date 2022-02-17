package com.example.schedule_clone.presentation.schedule

import androidx.lifecycle.ViewModel
import com.example.schedule_clone.domain.RefreshConferenceDataUseCase
import com.example.schedule_clone.domain.fcm.TopicSubscriber
import com.example.schedule_clone.domain.prefs.ScheduleUiHintsShownUseCase
import com.example.schedule_clone.domain.sessions.LoadScheduleUserSessionsUseCase
import com.example.schedule_clone.domain.sessions.ObserveConferenceDataUseCase
import com.example.schedule_clone.domain.settings.GetTimeZoneUseCase
import com.example.schedule_clone.presentation.messages.SnackMessageManager
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.shared.result.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flow
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val loadScheduleUserSessionsUseCase: LoadScheduleUserSessionsUseCase,
    signInViewModelDelegate: SignInViewModelDelegate,
    scheduleUiHintsShownUseCase: ScheduleUiHintsShownUseCase,
    topicSubscriber: TopicSubscriber,
    private val snackbarMessageManager: SnackMessageManager,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    private val refreshConferenceDataUseCase: RefreshConferenceDataUseCase,
    observeConferenceDataUseCase: ObserveConferenceDataUseCase
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate {

    // Exposed to the view as a StateFlow but it's a one-shot operation
    val timeZoneId = flow<ZoneId> {
        if (getTimeZoneUseCase(Unit).successOr(true)) {
            emit(TimeUnits.)
        }
    }
}