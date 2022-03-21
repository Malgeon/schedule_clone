package com.example.schedule_clone.presentation.filters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.schedule_clone.presentation.R
import com.example.schedule_clone.presentation.SectionHeader
import com.example.schedule_clone.shared.util.exceptionInDebug
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

/** Adapter for selectable filters, e.g. ones shown in the filter sheet. */
class SelectableFilterChipAdapter(
    private val viewModelDelegate: FiltersViewModelDelegate
) : ListAdapter<Any, ViewHolder>(FilterChipAndHeadingDiff) {

    companion object {
        private const val VIEW_TYPE_HEADING = R.layout.item_generic_section_header
        private const val VIEW_TYPE_FILTER = R.layout.item_filter_chip_selectable

        /**
         * Inserts category headings in a list of [FilterChip]s to make a heterogeneous list.
         * Assumes the items are already grouped by [FilterChip.categoryLabel], beginning with
         * categoryLabel == '0'
         */
        private fun insertCategoryHeadings(list: List<FilterChip>?): List<Any> {
            val newList = mutableListOf<Any>()
            var previousCategory = 0
            list?.forEach {
                val category = it.categoryLabel
                if (category != previousCategory && category != 0) {
                    newList += SectionHeader(
                        titleId = category,
                        useHorizontalPadding = false
                    )
                }
                newList.add(it)
                previousCategory = category
            }
            return newList
        }
    }

    override fun submitList(list: MutableList<Any>?) {
        exceptionInDebug(
            RuntimeException("call `submitEventFilterList()` instead to add category headings.")
        )
        super.submitList(list)
    }

    /** Prefer this method over [submitList] to add category headings. */
    fun submitFilterList(list: List<FilterChip>?) {
        super.submitList(insertCategoryHeadings(list))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SectionHeader -> VIEW_TYPE_HEADING
            is FilterChip -> VIEW_TYPE_FILTER
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADING -> createHeadingViewHolder()

        }
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