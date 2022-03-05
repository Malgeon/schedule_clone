package com.example.schedule_clone.presentation.schedule

import androidx.lifecycle.ViewModel
import com.example.schedule_clone.model.SessionId
import com.example.schedule_clone.presentation.sessioncommon.OnSessionClickListener
import com.example.schedule_clone.presentation.sessioncommon.OnSessionStarClickDelegate
import com.example.schedule_clone.shared.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


// Note: clients should obtain this from the Activity
@HiltViewModel
class ScheduleTwoPaneViewModel @Inject constructor(
    onSessionStarClickDelegate: OnSessionStarClickDelegate
) : ViewModel(),
    OnSessionClickListener,
    OnSessionStarClickDelegate by onSessionStarClickDelegate {

    private val _isTwoPane = MutableStateFlow(false)
    val isTwoPane: StateFlow<Boolean> = _isTwoPane

    private val _returnToListPaneEvents = Channel<Unit>(capacity = Channel.CONFLATED)
    val returnToListPaneEvents = _returnToListPaneEvents.receiveAsFlow()

    private val _selectSessionEvents = Channel<SessionId>(capacity = Channel.CONFLATED)
    val selectSessionEvents = _selectSessionEvents

    override fun openEventDetail(id: SessionId) {
        _selectSessionEvents.tryOffer(id)
    }

    fun setIsTwoPane(isTwoPane: Boolean) {
        _isTwoPane.value = isTwoPane
    }
}