package com.example.schedule_clone.presentation.sessioncommon

import com.example.schedule_clone.domain.users.StarEventAndNotifyUseCase
import com.example.schedule_clone.presentation.messages.SnackbarMessageManager
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * A delegate providing common functionality for starring events.
 */

interface OnSessionStarClickDelegate : OnSessionStarClickListener {
    val navigateToSignInDialogEvents: Flow<Unit>
}

class DefaultOnSessionStarClickDelegate @Inject constructor(
    signInViewModelDelegate: SignInViewModelDelegate,
    private val starEventUseCase: StarEventAndNotifyUseCase,
    private val snackbarMessageManager: SnackbarMessageManager,
    private val analyticsHelper: AnalyticsHelper,
    @ApplicationScope private val externalScope: CoroutineScope,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : OnSessionStarClickDelegate, SignInViewModelDelegate by signInViewModelDelegate {

}