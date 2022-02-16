package com.example.schedule_clone.presentation.schedule

import androidx.lifecycle.ViewModel
import com.example.schedule_clone.domain.fcm.TopicSubscriber
import com.example.schedule_clone.domain.prefs.ScheduleUiHintsShownUseCase
import com.example.schedule_clone.domain.sessions.LoadScheduleUserSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val loadScheduleUserSessionsUseCase: LoadScheduleUserSessionsUseCase,
    scheduleUiHintsShownUseCase: ScheduleUiHintsShownUseCase,
    topicSubscriber: TopicSubscriber,
    private val snackbarMessageManager: SnackMessageManager,

    ) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate {
}