package com.example.schedule_clone.presentation

import androidx.annotation.StringRes

data class SectionHeader(
    @StringRes val titleId: Int,
    val useHorizontalPadding: Boolean = true
)