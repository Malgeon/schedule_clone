package com.example.schedule_clone.presentation.filters

import androidx.activity.OnBackPressedCallback
import androidx.databinding.ObservableFloat
import androidx.fragment.app.Fragment
import com.example.schedule_clone.presentation.databinding.FragmentFiltersBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Fragment that shows the list of filters for teh Schedule
 */
abstract class FiltersFragment : Fragment() {

    companion object {
        // Threshold for when the filter sheet content should become invisible.
        // This should be a value between 0 and 1, coinciding with a point between the bottom
        // sheet's collapsed (0) and expanded (1) states.
        private const val ALPHA_CONTENT_START = 0.1f
        // Threshold for when the filter sheet content should become visible.
        // This should be a value between 0 and 1, coinciding with a point between the bottom
        // sheet's collapsed (0) and expanded (1) state.
        private const val ALPHA_CONTENT_END = 0.3f
    }

    private lateinit var viewModel: FiltersViewModelDelegate

    private lateinit var filterAdapter: SelectableFilterChipAdapter

    private lateinit var binding: FragmentFiltersBinding

    private lateinit var behavior: BottomSheetBehavior<*>

    private var contentAlpha = ObservableFloat(1f)

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            if (::behavior.isInitialized && behavior.state == STATE_EXPANDED)
        }
    }

}