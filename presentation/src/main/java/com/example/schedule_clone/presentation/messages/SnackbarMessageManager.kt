package com.example.schedule_clone.presentation.messages

import androidx.annotation.VisibleForTesting
import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.domain.prefs.StopSnackbarActionUseCase
import com.example.schedule_clone.shared.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A single source of Snackbar messages related to reservations.
 *
 * Only shows one Snackbar related to one change across all screens
 *
 * Emits new values on request (when a Snackbar is dismissed and ready to show a new message)
 *
 * It keeps a list of [MAX_ITEMS] items, enough to figure out if a message has already been shown,
 * but limited to avoid wasting resources.
 *
 */
@Singleton
open class SnackbarMessageManager @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
    @ApplicationScope private val coroutineScope: CoroutineScope,
    private val stopSnackbarActionUseCase: StopSnackbarActionUseCase
) {
    companion object {
        // Keep a fixed number of old items
        @VisibleForTesting
        const val MAX_ITEMS = 10
    }

    private val messages = mutableListOf<SnackbarMessage>()
}