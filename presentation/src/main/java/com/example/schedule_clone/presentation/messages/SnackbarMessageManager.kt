package com.example.schedule_clone.presentation.messages

import androidx.annotation.VisibleForTesting
import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.domain.prefs.StopSnackbarActionUseCase
import com.example.schedule_clone.shared.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SnackbarMessageManager @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @ApplicationScope private val coroutineScope: CoroutineScope,
    private val stopSnackberActionUseCase: StopSnackbarActionUseCase
) {
    companion object {
        // Keep a fixed number of old items
        @VisibleForTesting
        const val MAX_ITEMS = 10
    }
}