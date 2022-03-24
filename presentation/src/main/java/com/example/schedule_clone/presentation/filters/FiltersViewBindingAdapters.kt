package com.example.schedule_clone.presentation.filters

import android.content.res.ColorStateList
import android.graphics.Color
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule_clone.presentation.R
import com.google.android.material.chip.Chip

@BindingAdapter("activeFilters", "viewModel", requireAll = true)
fun activeFilters(
    recyclerView: RecyclerView,
    filters: List<FilterChip>?,
    viewModel: FiltersViewModelDelegate
) {
//    val filterChipAdapter: CloseableFilterChipAdapter

}

@BindingAdapter("showResultCount", "resultCount", requireAll = true)
fun filterHeader(textView: TextView, showResultCount: Boolean?, resultCount: Int?) {
    if (showResultCount == true && resultCount != null) {
        textView.text = textView.resources.getString(R.string.result_count, resultCount)
    } else {
        textView.setText(R.string.filters)
    }
}

@BindingAdapter("filterChipOnClick", "viewModel", requireAll = true)
fun filterChipOnClick(
    chip: Chip,
    filterChip: FilterChip,
    viewModel: FiltersViewModelDelegate
) {
    chip.setOnClickListener {
        chip.setOnCloseIconClickListener {
            viewModel.toggleFilter(filterChip.filter, !filterChip.isSelected)
        }
    }
}

@BindingAdapter("filterChipOnClose", "viewModel", requireAll = true)
fun filterChipOnClose(
    chip: Chip,
    filterChip: FilterChip,
    viewModel: FiltersViewModelDelegate
) {
    chip.setOnCloseIconClickListener {
        viewModel.toggleFilter(filterChip.filter, false)
    }
}

@BindingAdapter("filterChipText")
fun filterChipText(chip: Chip, filter: FilterChip) {
    if (filter.textResId != 0) {
        chip.setText(filter.textResId)
    } else {
        chip.text = filter.text
    }
}

@BindingAdapter("filterChipTint")
fun filterChipTint(chip: Chip, color: Int) {
    val tintColor = if (color != Color.TRANSPARENT) {
        color
    } else {
        ContextCompat.getColor(chip.context, R.color.default_tag_color)
    }
    chip.chipIconTint = ColorStateList.valueOf(tintColor)
}