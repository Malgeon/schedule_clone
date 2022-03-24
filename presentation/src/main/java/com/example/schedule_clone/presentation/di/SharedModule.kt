/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.schedule_clone.presentation.di

import com.example.schedule_clone.data.ConferenceDataRepository
import com.example.schedule_clone.data.ConferenceDataSource
import com.example.schedule_clone.data.db.AppDatabase
import com.example.schedule_clone.data.session.DefaultSessionRepository
import com.example.schedule_clone.data.session.SessionRepository
import com.example.schedule_clone.domain.userevent.DefaultSessionAndUserEventRepository
import com.example.schedule_clone.domain.userevent.SessionAndUserEventRepository
import com.example.schedule_clone.domain.userevent.UserEventDataSource
import com.example.schedule_clone.shared.config.AppConfigDataSource
import com.example.schedule_clone.domain.fake.FakeAppConfigDataSource
import com.example.schedule_clone.domain.fake.FakeConferenceDataSource
import com.example.schedule_clone.domain.fake.userevent.FakeUserEventDataSource
import com.example.schedule_clone.domain.fcm.StagingTopicSubscriber
import com.example.schedule_clone.domain.fcm.TopicSubscriber
import com.example.schedule_clone.domain.search.FtsMatchStrategy
import com.example.schedule_clone.domain.search.SessionTextMatchStrategy
import com.example.schedule_clone.domain.search.SimpleMatchStrategy
import com.example.schedule_clone.shared.di.SearchUsingRoomEnabledFlag
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

/**
 * Module where classes created in the shared module are created.
 */
@InstallIn(SingletonComponent::class)
@Module
class SharedModule {

// Define the data source implementations that should be used. All data sources are singletons.

    @Singleton
    @Provides
    @Named("remoteConfDatasource")
    fun provideConferenceDataSource(): ConferenceDataSource {
        return FakeConferenceDataSource
    }

    @Singleton
    @Provides
    @Named("bootstrapConfDataSource")
    fun provideBootstrapRemoteSessionDataSource(): ConferenceDataSource {
        return FakeConferenceDataSource
    }

    @Singleton
    @Provides
    fun provideConferenceDataRepository(
        @Named("remoteConfDatasource") remoteDataSource: ConferenceDataSource,
        @Named("bootstrapConfDataSource") boostrapDataSource: ConferenceDataSource,
        appDatabase: AppDatabase
    ): ConferenceDataRepository {
        return ConferenceDataRepository(remoteDataSource, boostrapDataSource, appDatabase)
    }

    @Singleton
    @Provides
    fun provideSessionRepository(
        conferenceDataRepository: ConferenceDataRepository
    ): SessionRepository {
        return DefaultSessionRepository(conferenceDataRepository)
    }

    @Singleton
    @Provides
    fun provideUserEventDataSource(): UserEventDataSource {
        return FakeUserEventDataSource
    }

//    @Singleton
//    @Provides
//    fun provideFeedbackEndpoint(): FeedbackEndpoint {
//        return FakeFeedbackEndpoint
//    }

    @Singleton
    @Provides
    fun provideSessionAndUserEventRepository(
        userEventDataSource: UserEventDataSource,
        sessionRepository: SessionRepository
    ): SessionAndUserEventRepository {
        return DefaultSessionAndUserEventRepository(
            userEventDataSource,
            sessionRepository
        )
    }

    @Singleton
    @Provides
    fun provideTopicSubscriber(): TopicSubscriber {
        return StagingTopicSubscriber()
    }

    @Singleton
    @Provides
    fun provideAppConfigDataSource(): AppConfigDataSource {
        return FakeAppConfigDataSource()
    }

//    @Singleton
//    @Provides
//    fun provideTimeProvider(): TimeProvider {
//        // TODO: Make the time configurable
//        return DefaultTimeProvider
//    }

//    @Singleton
//    @Provides
//    fun provideAnnouncementDataSource(): AnnouncementDataSource {
//        return FakeAnnouncementDataSource
//    }

//    @Singleton
//    @Provides
//    fun provideMomentDataSource(): MomentDataSource {
//        return FakeMomentDataSource
//    }

//    @Singleton
//    @Provides
//    fun provideFeedRepository(
//        announcementDataSource: AnnouncementDataSource,
//        momentDataSource: MomentDataSource
//    ): FeedRepository {
//        return DefaultFeedRepository(announcementDataSource, momentDataSource)
//    }

//    @Singleton
//    @Provides
//    fun provideArDebugFlagEndpoint(): ArDebugFlagEndpoint {
//        return FakeArDebugFlagEndpoint
//    }

    @Singleton
    @Provides
    fun provideSessionTextMatchStrategy(
        @SearchUsingRoomEnabledFlag useRoom: Boolean,
        appDatabase: AppDatabase
    ): SessionTextMatchStrategy {
        return if (useRoom) FtsMatchStrategy(appDatabase) else SimpleMatchStrategy
    }
}
