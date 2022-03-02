package com.example.schedule_clone.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.schedule_clone.data.db.AppDatabase
import com.example.schedule_clone.data.pref.PreferenceStorage
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.shared.analytics.AnalyticsHelper
import com.example.schedule_clone.shared.di.ApplicationScope
import com.example.schedule_clone.shared.di.DefaultDispatcher
import com.example.schedule_clone.shared.di.MainThreadHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

/**
 * Defines all the classes that need to be provided in the scope of the app.
 *
 * Define here all objects that are shared throughout the app, like SharedPreferences, navigators or
 * others. If some of those objects are singletons, they should be annotated with `@Singleton`.
 */
@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

    @ApplicationScope
    @Singleton
    @Provides
    fun providesApplicationScope(
        @DefaultDispatcher defaultDispatcher: CoroutineDispatcher
    ): CoroutineScope = CoroutineScope(SupervisorJob() + defaultDispatcher)

//    @Singleton
//    @Provides
//    @MainThreadHandler
//    fun provideMainThreadHandler(): IOSchedHandler = IOSchedMainHandler()

    @Singleton
    @Provides
    fun provideAnalyticsHelper(
        @ApplicationScope applicationScope: CoroutineScope,
        signInDelegate: SignInViewModelDelegate,
        preferenceStorage: PreferenceStorage
    ): AnalyticsHelper =
        FirebaseAnayticsHelper(applicationScope, signInDelegate, preferenceStorage)

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.buildDatabase(context)
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }
}