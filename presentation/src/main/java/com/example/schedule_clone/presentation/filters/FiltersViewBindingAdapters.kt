package com.example.schedule_clone.presentation.filters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("activeFilters", "viewModel", requireAll = true)
fun activeFilters(
    recyclerView: RecyclerView,
    filters: List<FilterChip>?,
    viewModel: FiltersViewModelDelegate
) {

}