package com.example.schedule_clone.presentation.auth

import com.example.schedule_clone.data.signin.AuthenticatedUserInfo
import com.example.schedule_clone.data.signin.datasources.AuthStateUserDataSource
import com.example.schedule_clone.data.signin.datasources.RegisteredUserDataSource
import com.example.schedule_clone.domain.FlowUseCase
import com.example.schedule_clone.domain.fcm.TopicSubscriber
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.IoDispatcher
import com.example.schedule_clone.shared.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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

    override fun execute(parameters: Any): Flow<Result<AuthenticatedUserInfo>> {
        TODO("Not yet implemented")
    }
}