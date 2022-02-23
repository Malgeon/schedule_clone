package com.example.schedule_clone.presentation.sessioncommon

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.schedule_clone.presentation.R

@BindingAdapter("tagTint")
fun tagTint(textView: TextView, color: Int) {
    // Tint the colored dot
    (textView.compoundDrawablesRelative[0] as? GradientDrawable)?.setColor(
        tagTintOrDefault(
            color,
            textView.context
        )
    )
}

fun tagTintOrDefault(color: Int, context: Context): Int {
    return if (color != Color.TRANSPARENT) {
        color
    } else {
        ContextCompat.getColor(context, R.color.default_tag_color)
    }
}