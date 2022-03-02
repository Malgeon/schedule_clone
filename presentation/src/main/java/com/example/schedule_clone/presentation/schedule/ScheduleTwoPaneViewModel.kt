package com.example.schedule_clone.presentation.schedule

import androidx.lifecycle.ViewModel
import com.example.schedule_clone.presentation.sessioncommon.OnSessionClickListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


// Note: clients should obtain this from the Activity
@HiltViewModel
class ScheduleTwoPaneViewModel @Inject constructor(
    onSessionStarClickListener: OnSessionStarClickDelegate
) : ViewModel(),
OnSessionClickListener,
OnSessionStarDelegate by {
}