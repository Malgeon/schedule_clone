package com.example.schedule_clone.presentation.schedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule_clone.domain.RefreshConferenceDataUseCase
import com.example.schedule_clone.domain.fcm.TopicSubscriber
import com.example.schedule_clone.domain.prefs.ScheduleUiHintsShownUseCase
import com.example.schedule_clone.domain.sessions.*
import com.example.schedule_clone.domain.settings.GetTimeZoneUseCase
import com.example.schedule_clone.presentation.messages.SnackMessageManager
import com.example.schedule_clone.presentation.sessioncommon.stringRes
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.presentation.util.WhileViewSubscribed
import com.example.schedule_clone.shared.result.successOr
import com.example.schedule_clone.shared.result.Result.Success
import com.example.schedule_clone.shared.result.Result.Error
import com.example.schedule_clone.shared.util.TimeUtils
import com.example.schedule_clone.shared.util.tryOffer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow.DROP_LATEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.launch
import org.threeten.bp.ZoneId
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val loadScheduleUserSessionsUseCase: LoadScheduleUserSessionsUseCase,
    signInViewModelDelegate: SignInViewModelDelegate,
    scheduleUiHintsShownUseCase: ScheduleUiHintsShownUseCase,
    topicSubscriber: TopicSubscriber,
    private val snackbarMessageManager: SnackbarMessageManager,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    private val refreshConferenceDataUseCase: RefreshConferenceDataUseCase,
    observeConferenceDataUseCase: ObserveConferenceDataUseCase
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate {

    // Exposed to the view as a StateFlow but it's a one-shot operation
    val timeZoneId = flow<ZoneId> {
        if (getTimeZoneUseCase(Unit).successOr(true)) {
            emit(TimeUtils.CONFERENCE_TIMEZONE)
        } else {
            emit(ZoneId.systemDefault())
        }
    }.stateIn(viewModelScope, Lazily, TimeUtils.CONFERENCE_TIMEZONE)

    val isConferenceTimeZone: StateFlow<Boolean> = timeZoneId.mapLatest { zoneId ->
        TimeUtils.isConferenceTimeZone(zoneId)
    }.stateIn(viewModelScope, Lazily, true)

    private lateinit var dayIndexer: ConferenceDayIndexer

    // Used to re-run flows on command
    private val refreshSignal = MutableSharedFlow<Unit>()
    // Used to run flows on init and also on command
    private val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    // Event coming from repository indicating data should be refreshed
    init {
        viewModelScope.launch {
            observeConferenceDataUseCase(Unit).collect {
                refreshUserSessions()
            }
        }
    }

    // Latest user ID
    private val currentUserId = userId.stateIn(viewModelScope, WhileViewSubscribed, null)

    // Refresh sessions when needed and when the user changes
    private val loadSessionsResult: StateFlow<Result<LoadScheduleUserSessionsResult>> =
        loadDataSignal.combineTransform(currentUserId) { _, userId ->
            emitAll(
                loadScheduleUserSessionsUseCase(
                    LoadScheduleUserSessionsParameters(userId)
                )
            )
        }
            .onEach {
                // Side effect: show error messages coming from LoadScheduleUserSessionsUseCase
                if (it is Error) {
                    _errorMessage.tryOffer(it.exception.message ?: "Error")
                }
                // Side effect: show snackbar if the result contains a message
                if (it is Success) {
                    it.data.userMessage?.type?.stringRes()?.let { messageId ->
                        // There is a message to display:
                        snackbarMessageManager.addMessage(
                            SnackbarMessage(
                                messageId = messageId,
                                longDuration = true,
                                session = it.data.userMessageSession,
                                requestChangeId = it.data.userMessage?.changeRequestId
                            )
                        )
                    }
                }
            }
            .stateIn(viewModelScope, WhileViewSubscribed, Result.Loading)

    val isLoading: StateFlow<Boolean> = loadSessionsResult.mapLatest {
        it = Result.Loading
    }.stateIn(viewModelScope, WhileViewSubscribed, ture)

    // Expose new UI data when loadSessionsResult changes
    val scheduleUiData: StateFlow<ScheduleUiData> =
        loadSessionsResult.combineTransform(timeZoneId) { sessions, timeZone ->

        }

    /** Flows for Actions and Events **/

    // SIDE EFFECTS: Error messages
    // Guard against too many error messages by limiting to 3, keeping the oldest.
    private val _errorMessage = Channel<String>(1, DROP_LATEST)
    val errorMessage: Flow<String> =
        _errorMessage.receiveAsFlow().shareIn(viewModelScope, WhileViewSubscribed)

    private fun refreshUserSessions() {
        refreshSignal.tryEmit(Unit)
    }
}