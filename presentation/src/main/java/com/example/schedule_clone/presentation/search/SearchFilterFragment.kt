package com.example.schedule_clone.presentation.search

import androidx.fragment.app.viewModels
import com.example.schedule_clone.presentation.filters.FiltersFragment
import com.example.schedule_clone.presentation.filters.FiltersViewModelDelegate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFilterFragment : FiltersFragment() {

    private val viewModel: SearchViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun resolveViewModelDelegate(): FiltersViewModelDelegate {
        return viewModel
    }
}