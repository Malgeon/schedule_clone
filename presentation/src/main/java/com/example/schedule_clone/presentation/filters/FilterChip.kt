package com.example.schedule_clone.presentation.filters

import android.graphics.Color
import com.example.schedule_clone.presentation.R
import com.example.schedule_clone.model.Tag
import com.example.schedule_clone.model.filters.Filter
import com.example.schedule_clone.model.filters.Filter.DateFilter
import com.example.schedule_clone.model.filters.Filter.MyScheduleFilter
import com.example.schedule_clone.model.filters.Filter.TagFilter
import com.example.schedule_clone.shared.util.TimeUtils

/** Wrapper model for showing [Filter] as a chip in the UI. */
data class FilterChip(
    val filter: Filter,
    val isSelected: Boolean,
    val categoryLabel: Int = 0,
    val color: Int = Color.parseColor("#4768fd"), // @color/indigo
    val selectedTextColor: Int = Color.WHITE,
    val textResId: Int = 0,
    val text: String = ""
)

fun Filter.asChip(isSelected: Boolean): FilterChip = when (this) {
    is TagFilter -> FilterChip(
        filter = this,
        isSelected = isSelected,
        color = tag.color,
        text = tag.displayName,
        selectedTextColor = tag.fontColor ?: Color.TRANSPARENT,
        categoryLabel = tag.filterCategoryLabel()
    )
    is DateFilter -> FilterChip(
        filter = this,
        isSelected = isSelected,
        textResId = TimeUtils.getShortLabelResForDay(day),
        categoryLabel = R.string.category_heading_dates
    )
    MyScheduleFilter -> FilterChip(
        filter = this,
        isSelected = isSelected,
        textResId = R.string.my_events,
        categoryLabel = R.string.category_heading_dates
    )
}

private fun Tag.filterCategoryLabel(): Int = when (this.category) {
    Tag.CATEGORY_TYPE -> R.string.category_heading_types
    Tag.CATEGORY_TOPIC -> R.string.category_heading_tracks
    Tag.CATEGORY_LEVEL -> R.string.category_heading_levels
    else -> 0
}