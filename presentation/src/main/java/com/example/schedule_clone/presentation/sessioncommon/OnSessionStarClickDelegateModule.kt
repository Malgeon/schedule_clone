package com.example.schedule_clone.presentation.sessioncommon

import com.example.schedule_clone.domain.users.StarEventAndNotifyUseCase
import com.example.schedule_clone.presentation.messages.SnackbarMessageManager
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.shared.analytics.AnalyticsHelper
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.MainDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

/**
 * Provides a default implementation of [OnSessionStarClickDelegate].
 */
@InstallIn(ViewModelComponent::class)
@Module
internal class OnSessionStarClickDelegateModule {

    @Provides
    fun provideOnSessionStarClickDelegate(
        signInViewModelDelegate: SignInViewModelDelegate,
        starEventUseCase: StarEventAndNotifyUseCase,
        snackbarMessageManager: SnackbarMessageManager,
        analyticsHelper: AnalyticsHelper,
        @ApplicationScope applicationScope: CoroutineScope,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): OnSessionStarClickDelegate {
        return DefaultOnSessionStarClickDelegate(
            signInViewModelDelegate,
            starEventUseCase,
            snackbarMessageManager,
            analyticsHelper,
            applicationScope,
            mainDispatcher
        )
    }
}