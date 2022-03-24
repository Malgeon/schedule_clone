package com.example.schedule_clone.presentation.filters

import com.example.schedule_clone.shared.di.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@InstallIn(SingletonComponent::class)
@Module
class FiltersViewModelDelegateModule {

    @Provides
    fun provideFiltersViewModelDelegate(
        @ApplicationScope applicationScope: CoroutineScope
    ): FiltersViewModelDelegate = FiltersViewModelDelegateImpl(applicationScope)
}