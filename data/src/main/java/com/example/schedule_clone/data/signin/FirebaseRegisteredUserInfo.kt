package com.example.schedule_clone.data.signin

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserInfo

/**
 * Delegates [AuthenticatedUserInfo] calls to a [FirebaseUser] to be used in production.
 */
class FirebaseRegisteredUserInfo(
    private val basicUserInfo: AuthenticatedUserInfoBasic?,
    private val isRegistered: Boolean?
) : AuthenticatedUserInfo {

    override fun isSignedIn(): Boolean = isRegistered ?: false

    override fun getEmail(): String? {
        TODO("Not yet implemented")
    }

    override fun getProviderData(): MutableList<out UserInfo>? {
        TODO("Not yet implemented")
    }

    override fun getLastSignInTimestamp(): Long? {
        TODO("Not yet implemented")
    }

    override fun getCreationTimestamp(): Long? {
        TODO("Not yet implemented")
    }

    override fun isAnonymous(): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String? {
        TODO("Not yet implemented")
    }

    override fun getUid(): String? {
        TODO("Not yet implemented")
    }

    override fun isEmailVerified(): String? {
        TODO("Not yet implemented")
    }

    override fun getPhotoUrl(): Uri? {
        TODO("Not yet implemented")
    }

    override fun getProviderId(): String? {
        TODO("Not yet implemented")
    }

    override fun isRegistered(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isRegistrationDataReady(): Boolean {
        TODO("Not yet implemented")
    }
}

open class FirebaseUserInfo(
    private val firebaseUser: FirebaseUser?
) : AuthenticatedUserInfoBasic {
    override fun isSignedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getEmail(): String? {
        TODO("Not yet implemented")
    }

    override fun getProviderData(): MutableList<out UserInfo>? {
        TODO("Not yet implemented")
    }

    override fun getLastSignInTimestamp(): Long? {
        TODO("Not yet implemented")
    }

    override fun getCreationTimestamp(): Long? {
        TODO("Not yet implemented")
    }

    override fun isAnonymous(): Boolean? {
        TODO("Not yet implemented")
    }

    override fun getPhoneNumber(): String? {
        TODO("Not yet implemented")
    }

    override fun getUid(): String? {
        TODO("Not yet implemented")
    }

    override fun isEmailVerified(): String? {
        TODO("Not yet implemented")
    }

    override fun getPhotoUrl(): Uri? {
        TODO("Not yet implemented")
    }

    override fun getProviderId(): String? {
        TODO("Not yet implemented")
    }
}