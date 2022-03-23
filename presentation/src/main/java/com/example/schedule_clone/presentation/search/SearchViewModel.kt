package com.example.schedule_clone.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.schedule_clone.domain.search.LoadSearchFiltersUseCase
import com.example.schedule_clone.domain.search.SessionSearchUseCase
import com.example.schedule_clone.domain.settings.GetTimeZoneUseCase
import com.example.schedule_clone.model.userdata.UserSession
import com.example.schedule_clone.presentation.filters.FiltersViewModelDelegate
import com.example.schedule_clone.presentation.signin.SignInViewModelDelegate
import com.example.schedule_clone.shared.analytics.AnalyticsHelper
import com.example.schedule_clone.shared.result.Result
import com.example.schedule_clone.shared.result.Result.Loading
import com.example.schedule_clone.shared.result.successOr
import com.example.schedule_clone.shared.util.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.threeten.bp.ZoneId
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
    val searchResults: StateFlow<List<UserSession>> = _searchResults

    private val _isEmpty = MutableStateFlow(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    private var searchJob: Job? = null

    val timeZoneId: StateFlow<ZoneId> = flow {
        if (getTimeZoneUseCase(Unit).successOr(true)) {
            emit(TimeUtils.CONFERENCE_TIMEZONE)
        } else {
            emit(ZoneId.systemDefault())
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, ZoneId.systemDefault())

    private var textQuery = ""

    // Override because we also want to show result count when there's a text query.
    private val _showResultCount = MutableStateFlow(false)
    override val showResultCount: StateFlow<Boolean> = _showResultCount

    init {
        // Load filters
        viewModelScope.launch {
            setSupportedFilters(loadFilterUseCase(Unit).successOr(emptyList()))
        }

        // Re-execute search when selected filters change
        viewModelScope.launch {
            selectedFilters.collect {
                executeSearch()
            }
        }
    }

    private fun executeSearch() {
        // Cancel any in-flight searches
        searchJob?.cancel()

        val filters = selectedFilters.value
        if (textQuery.isEmpty() && filters.isEmpty()) {
            clearSearchResults()
        }

        searchJob = viewModelScope.launch {
            // The user could be typing or toggling filters rapidly. Giving the search job
            // a slight delay and cancelling it on each call to this method effectively debounces.
        }
    }

    private fun clearSearchResults() {
        _searchResults.value = emptyList()
        // Explicitly set false to not show the "No results" state
        _isEmpty.value = false
        _showResultCount.value = false
        resultCount.value = 0
    }

    private fun processSearchResult(searchResult: Result<List<UserSession>>) {
        if (searchResult is Loading) {
            return // avoid UI flickering
        }
        val sessions = searchResult.successOr(emptyList())
        _searchResults.value = sessions
        _isEmpty.value = sessions.isEmpty()
        _showResultCount.value = true
        resultCount.value = sessions.size
    }
}