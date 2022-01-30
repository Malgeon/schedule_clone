package com.example.schedule_clone.model.filters

import com.example.schedule_clone.model.ConferenceDay
import com.example.schedule_clone.model.Tag

sealed class Filter {

    data class TagFilter(val tag: Tag) : Filter()

    data class DateFilter(val day: ConferenceDay) : Filter()

    object MyScheduleFilter : Filter()
}
