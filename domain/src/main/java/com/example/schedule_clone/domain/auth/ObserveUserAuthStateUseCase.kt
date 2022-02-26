package com.example.schedule_clone.domain.auth

import com.example.schedule_clone.data.signin.AuthenticatedUserInfo
import com.example.schedule_clone.data.signin.AuthenticatedUserInfoBasic
import com.example.schedule_clone.data.signin.datasources.AuthStateUserDataSource
import com.example.schedule_clone.data.signin.datasources.RegisteredUserDataSource
import com.example.schedule_clone.domain.FlowUseCase
import com.example.schedule_clone.domain.fcm.TopicSubscriber
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.shared.result.Result.Success
import com.example.schedule_clone.shared.util.cancelIfActive
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A [FlowUseCase] that observes two data sources to generate an [AuthenticatedUserInfo]
 * that includes whether the user is registered (is an attendee).
 *
 * [AuthStateUserDataSource] provides general user information, like user IDs, while
 * [RegisteredUserDataSource] observes a different data source to provide a flag indicating
 * whether the user is registered.
 */
@Singleton
open class ObserveUserAuthStateUseCase @Inject constructor(
    private val registeredUserDataSource: RegisteredUserDataSource,
    private val authStateUserDataSource: AuthStateUserDataSource,
    private val topicSubscriber: TopicSubscriber,
    @ApplicationScope private val externalScope: CoroutineScope,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Any, AuthenticatedUserInfo>(ioDispatcher) {

    private var observeUserRegisteredChangeJob: Job? = null

    // As a separate coroutine needs to listen for user registration changes and emit to the
    // flow, a callbackFlow is used
    private val authStateChanges = callbackFlow<Result<AuthenticatedUserInfo>> {
        authStateUserDataSource.getBasicUserInfo().collect { userResult ->
        // Cancel observing previous user registered changes
        observeUserRegisteredChangeJob.cancelIfActive()'

            if(userResult is Success) {
                if (userResult.data != null) {
                    processUserData
                }
            }

        }

    }

    override fun execute(parameters: Any): Flow<Result<AuthenticatedUserInfo>> {
        TODO("Not yet implemented")
    }

    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.processUserData(
        userData: AuthenticatedUserInfoBasic
    ) {
        if (!userData.isSignedIn()) {
            userSignedOut(userData)
        } else if
    }

    private suspend fun ProducerScope<Result<AuthenticatedUserInfo>>.userSignedOut(
        userData: AuthenticatedUserInfoBasic?
    ) {
        send(Success(FirebaseRegisteredUserInfo(userData, isRegisteredValue)))
        unscribeFromRegisteredTopic() // Stop receiving notifications for attendees
    }
}