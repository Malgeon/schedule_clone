package com.example.schedule_clone.presentation.signin

import android.net.Uri
import com.example.schedule_clone.data.signin.AuthenticatedUserInfo
import com.example.schedule_clone.presentation.auth.ObserveUserAuthStateUseCase
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.di.MainDispatcher
import com.example.schedule_clone.shared.di.ReservationEnabledFlag
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

enum class SignInNavigationAction {
    RequestSignIn, RequestSignOut, ShowNotificationPreferencesDialog
}

/**
 * Interface to implement sign-in functionality in a ViewModel.
 *
 * you can inject a implementation of this via Dagger2, then use the implementation as an interface
 * delegate to add sign in functionality without writing any code
 *
 * Example usage
 *
 * ```
 * class MyViewModel @Injecty constructor(
 *     signInViewModelComponent: SignInViewModelDelegate
 * ) : ViewModel(), SignInViewModelDelegate by signInViewModelComponent {
 * ```
 *
 */
interface SignInViewModelDelegate {
    /**
     * Live updated value of the current firebase user
     */
    val userInfo: StateFlow<AuthenticatedUserInfo?>

    /**
     * Live updated value of the current firebase users image url
     */
    val currentUserImageUri: StateFlow<Uri?>

    /**
     * Emits Event when a sign-in event should be attempted or a dialog shown
     */
    val signInNavigationActions: Flow<SignInNavigationAction>

    /**
     * Emits whether or not to show reservations for the current user
     */
    val showReservations: StateFlow<Boolean>

    /**
     * Emit an Event on performSignInEvent to request sign-in
     */
    suspend fun emitSignInRequest()

    /**
     * Emit an Event on performSignInEvent to request sign-out
     */
    suspend fun emitSignOutRequest()

    val userId: Flow<String?>

    /**
     * Returns the current user ID or null if not available.
     */
    val userIdValue: String?

    val isUserSignedIn: StateFlow<Boolean>

    val isUserSignedInValue: Boolean

    val isUserRegistered: StateFlow<Boolean>

    val isUserRegisteredValue: Boolean
}

/**
 * Implementation of SignInViewModelDelegate that uses Firebase's auth mechanism.
 */
internal class FirebaseSignInViewModelDelegate @Inject constructor(
    observeUserAuthStateUseCase: ObserveUserAuthStateUseCase,
    private val notificationsPrefIsShownUseCase: NotificationsPrefIsShownUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    @ReservationEnabledFlag val isReservationEnabledByRemoteConfig: Boolean,
    @ApplicationScope val applicationScope: CoroutineScope
) : SignInViewModelDelegate {


}