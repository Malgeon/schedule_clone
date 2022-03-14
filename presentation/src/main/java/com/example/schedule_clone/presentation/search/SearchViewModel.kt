package com.example.schedule_clone.presentation.search

import androidx.lifecycle.ViewModel
import com.example.schedule_clone.domain.search.LoadSearchFiltersUseCase
import com.example.schedule_clone.domain.search.SessionSearchUseCase
import com.example.schedule_clone.domain.settings.GetTimeZoneUseCase
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.presentation.filters.FiltersViewModelDelegate
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.shared.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val searchUseCase: SessionSearchUseCase,
    getTimeZoneUseCase: GetTimeZoneUseCase,
    loadFilterUseCase: LoadSearchFiltersUseCase,
    signInViewModelDelegate: SignInViewModelDelegate,
    filtersViewModelDelegate: FiltersViewModelDelegate
) : ViewModel(),
    SignInViewModelDelegate by signInViewModelDelegate,
    FiltersViewModelDelegate by filtersViewModelDelegate {

    private val _searchResults = MutableStateFlow<List<UserSession>>(emptyList())

}