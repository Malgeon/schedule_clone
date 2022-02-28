package com.example.schedule_clone.presentation.signin

import com.example.schedule_clone.domain.auth.ObserveUserAuthStateUseCase
import com.example.schedule_clone.domain.prefs.NotificationsPrefIsShownUseCase
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.di.MainDispatcher
import com.example.schedule_clone.shared.di.ReservationEnabledFlag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SIgnInViewModelDelegateModule {

    @Singleton
    @Provides
    fun provideSignInViewModelDelegate(
        dataSource: ObserveUserAuthStateUseCase,
        notificationsPrefIsShownUseCase: NotificationsPrefIsShownUseCase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @MainDispatcher mainDispatcher: CoroutineDispatcher,
        @ReservationEnabledFlag isReservationEnabledByRemoteConfig: Boolean,
        @ApplicationScope applicationScope: CoroutineScope
    ): SignInViewModelDelegate {
        return FirebaseSignInViewModelDelegate(
            observeUserAuthStateUseCase = dataSource,
            notificationsPrefIsShownUseCase = notificationsPrefIsShownUseCase,
            ioDispatcher = ioDispatcher,
            mainDispatcher = mainDispatcher,
            isReservationEnabledByRemoteConfig = isReservationEnabledByRemoteConfig,
            applicationScope = applicationScope
        )
    }
}