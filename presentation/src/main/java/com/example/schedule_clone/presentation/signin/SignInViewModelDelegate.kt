package com.example.schedule_clone.presentation.signin

import kotlinx.coroutines.flow.StateFlow

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
}