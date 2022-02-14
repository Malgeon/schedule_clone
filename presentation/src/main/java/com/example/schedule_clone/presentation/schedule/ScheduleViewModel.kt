package com.example.schedule_clone.presentation.schedule

import com.example.schedule_clone.domain.sessions.LoadScheduleUserSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val loadScheduleUserSessionsUseCase: LoadScheduleUserSessionsUseCase

) {
}