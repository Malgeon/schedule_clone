package com.example.schedule_clone.presentation.util

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.clearDecorations() {
    if (itemDecorationCount > 0) {
        for (i in itemDecorationCount -1 downTo 0) {
            removeItemDecorationAt(i)
        }
    }
}