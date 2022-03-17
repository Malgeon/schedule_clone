package com.example.schedule_clone.presentation.widget

import android.view.View
import androidx.annotation.IntDef
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior

/**
 * Copy of material lib's BottomSheetBehavior that includes some bug fixes
 */
// TODO remove when a fixed version in material lib is released.
class BottomSheetBehavior<V : View> : Behavior<V> {

    companion object {
        /** The bottom sheet is dragging. */
        const val STATE_DRAGGING = 1
        /** The bottom sheet is settling. */
        const val STATE_SETTLING = 2
        /** The bottom sheet is expanded. */
        const val STATE_EXPANDED = 3
        /** The bottom sheet is collapsed. */
        const val STATE_COLLAPSED = 4
        /** The bottom sheet is hidden. */
        const val STATE_HIDDEN = 5
        /** The bottom sheet is half-expanded (used when behavior_fitToContents is false). */
        const val STATE_HALF_EXPANDED = 6

        /**
         * Peek at the 16:9 ratio keyline of its parent. This can be used as a parameter for
         * [setPeekHeight(Int)]. [getPeekHeight()] will return this when the value is set.
         */
        const val PEEK_HEIGHT_AUTH = -1

        private const val HIDE_THRESHOLD = 0.5f
        private const val HIDE_FRICTION = 0.1f

        @IntDef(
            value = [
                STATE_DRAGGING,
                STATE_SETTLING,
                STATE_EXPANDED,
                STATE_COLLAPSED,
                STATE_HIDDEN,
                STATE_HALF_EXPANDED
            ]
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class State

    }
}