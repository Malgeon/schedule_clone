package com.example.schedule_clone.presentation.widget

import android.content.Context
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * ItemDecoration that draws a bubble background around items in a specified range.
 */
class BubbleDecoration(context: Context) : ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = FILL
    }
}