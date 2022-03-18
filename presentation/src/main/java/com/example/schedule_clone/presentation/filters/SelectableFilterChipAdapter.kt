package com.example.schedule_clone.presentation.filters

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.schedule_clone.presentation.R

/** Adapter for selectable filters, e.g. ones shown in the filter sheet. */
class SelectableFilterChipAdapter(
    private val viewModelDelegate: FiltersViewModelDelegate
) : ListAdapter<Any, ViewHolder>(FilterChipAndHeadingDiff) {

    companion object {
        private const val VIEW_TYPE_HEADING = R.layout.item_generic_section_header
        private const val VIEW_TYPE_FILTER = R.layout.item_filter_chip_selectable

        /**
         * Inserts category headings in a list of [FilterChip]s to make a heterogeneous list.
         */
    }
}

private object FilterChipAndHeadingDiff : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when (oldItem) {
            is FilterChip -> newItem is FilterChip && newItem.filter == oldItem.filter
            else -> oldItem == newItem // SectionHeader
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when (oldItem) {
            is FilterChip -> oldItem.isSelected == (newItem as FilterChip).isSelected
            else -> true
        }
    }
}