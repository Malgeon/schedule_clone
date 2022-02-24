package com.example.schedule_clone.data.signin.datasources

import com.example.schedule_clone.data.signin.AuthenticatedUserInfoBasic
import com.example.schedule_clone.shared.result.Result
import kotlinx.coroutines.flow.Flow

/**
 * Listens to an Authentication state data source that emits updates on the current user.
 *
 * @see FirebaseAuthStateUserDataSource
 */
interface AuthStateUserDataSource {
    /**
     * Returns an observable of the [AuthenticatedUserInfoBasic].
     */
    fun getBasicUserInfo(): Flow<Result<AuthenticatedUserInfoBasic?>>
}