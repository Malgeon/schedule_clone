package com.example.schedule_clone.presentation.filters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.OnBackPressedCallback
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.databinding.ObservableFloat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.example.schedule_clone.presentation.R
import com.example.schedule_clone.presentation.databinding.FragmentFiltersBinding
import com.example.schedule_clone.presentation.util.doOnApplyWindowInsets
import com.example.schedule_clone.presentation.widget.BottomSheetBehavior
import com.example.schedule_clone.presentation.widget.BottomSheetBehavior.BottomSheetCallback
import com.example.schedule_clone.presentation.widget.BottomSheetBehavior.STATE_COLLAPSED
import com.example.schedule_clone.presentation.widget.BottomSheetBehavior.STATE_EXPANDED
import com.example.schedule_clone.presentation.widget.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.flexbox.FlexboxItemDecoration

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
            if (::behavior.isInitialized && behavior.state == STATE_EXPANDED) {
                behavior.state = STATE_HIDDEN
            }
        }
    }

    private var pendingSheetState = -1

    /** Resolve the [FiltersViewModelDelegate] for this instance. */
    abstract fun resolveViewModelDelegate(): FiltersViewModelDelegate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiltersBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            contentAlpha = this@FiltersFragment.contentAlpha
        }

        // Pad the bottom of the RecyclerView so that the content scrolls up above the nav bar
        binding.recyclerviewFilters.doOnApplyWindowInsets { v, insets, padding ->
            val systemInsets = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            v.updatePadding(bottom = padding.bottom + systemInsets.bottom)
        }
        return binding.root
    }

    // In order to acquire the behavior associated with this sheet, we need to be attached to the
    // view hierarchy of our parent, otherwise we get an exception that our view is not a child of a
    // CoordinatorLayout. Therefore we do most initialization here instead of in onViewCreated().
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = resolveViewModelDelegate()
        binding.viewModel = viewModel

        behavior = BottomSheetBehavior.from(binding.filterSheet)

        filterAdapter = SelectableFilterChipAdapter(viewModel)
        binding.recyclerviewFilters.apply {
            adapter = filterAdapter
            setHasFixedSize(true)
            itemAnimator = null
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    binding.filtersHeaderShadow.isActivated = recyclerView.canScrollVertically(-1)
                }
            })
            addItemDecoration(
                FlexboxItemDecoration(context).apply {
                    setDrawable(context.getDrawable(R.drawable.divider_empty_margin_small))
                    setOrientation(FlexboxItemDecoration.VERTICAL)
                }
            )
        }

        // Update the peek and margins so that it scrolls and rests within sys ui
        val peekHeight = behavior.peekHeight
        val marginBottom = binding.root.marginBottom
        binding.root.doOnApplyWindowInsets { v, insets, _ ->
            val gestureInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures())
            // Update the peek height so that it is above the navigation bar
            behavior.peekHeight = gestureInsets.bottom + peekHeight

            v.updateLayoutParams<MarginLayoutParams> {  }
        }

        behavior.addBottomSheetCallback(object : BottomSheetCallback {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                updateFilterCon
            }
        })
    }

    private fun updateFilterContentsAlpha(slideOffset: Float) {

    }


}