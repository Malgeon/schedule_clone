package com.example.schedule_clone.presentation.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule_clone.domain.sessions.ConferenceDayIndexer
import com.example.schedule_clone.model.ConferenceDay
import com.example.schedule_clone.presentation.R
import com.example.schedule_clone.presentation.databinding.FragmentScheduleBinding
import com.example.schedule_clone.presentation.sessioncommon.SessionsAdapter
import com.example.schedule_clone.presentation.signin.setupProfileMenuItem
import com.example.schedule_clone.presentation.util.launchAndRepeatWithViewLifecycle
import com.example.schedule_clone.presentation.widget.BubbleDecoration
import com.example.schedule_clone.presentation.widget.FadingSnackbar
import com.example.schedule_clone.shared.analytics.AnalyticsActions
import com.example.schedule_clone.shared.analytics.AnalyticsHelper
import com.example.schedule_clone.shared.di.SearchScheduleEnabledFlag
import com.example.schedule_clone.shared.util.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    companion object {
        private const val DIALOG_NEED_TO_SIGN_IN = "dialog_need_to_sign_in"
        private const val DIALOG_CONFIRM_SIGN_OUT = "dialog_need_to_sign_in"
        private const val DIALOG_SCHEDULE_HINTS = "dialog_need_to_sign_in"
    }

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    @field:Named("tagViewPool")
    lateinit var tagViewPool: RecyclerView.RecycledViewPool

    @Inject
    @JvmField
    @SearchScheduleEnabledFlag
    var searchScheduleFeatureEnabled: Boolean = false

    private val scheduleViewModel: ScheduleViewModel by viewModels()
    private val scheduleTwoPaneViewModel: ScheduleTwoPaneViewModel by activityViewModels()

    private lateinit var snackbar: FadingSnackbar

    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var sessionsAdapter: SessionsAdapter

    private lateinit var dayIndicatorRecyclerView: RecyclerView
    private lateinit var dayIndicatorAdapter: DayIndicatorAdapter
    private lateinit var dayIndicatorItemDecoration: BubbleDecoration

    private lateinit var dayIndexer: ConferenceDayIndexer
    private var cachedBubbleRange: IntRange? = null

    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = scheduleViewModel
        }

        snackbar = binding.snackbar
        scheduleRecyclerView = binding.recyclerviewSchedule
        dayIndicatorRecyclerView = binding.dayIndicators
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up search menu item
        binding.toolbar.run {
            inflateMenu(R.menu.schedule_menu)
            menu.findItem(R.id.search).isVisible = searchScheduleFeatureEnabled
            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.search) {
                    analyticsHelper.logUiEvent("Navigation to Search", AnalyticsActions.CLICK)
                    openSearch()
                    true
                } else {
                    false
                }
            }
        }

        binding.toolbar.setupProfileMenuItem()

        // Session list configuration
        sessionsAdapter = SessionsAdapter(
            tagViewPool,
            scheduleViewModel.showReservations,
            scheduleViewModel.timeZoneId,
            viewLifecycleOwner,
            scheduleTwoPaneViewModel,
            scheduleTwoPaneViewModel
        )
        scheduleRecyclerView.apply {
            adapter = sessionsAdapter
            (itemAnimator as DefaultItemAnimator).run {
                supportsChangeAnimations = false
                addDuration = 160L
                moveDuration = 160L
                changeDuration = 160L
                removeDuration = 120L
            }
        }

        launchAndRepeatWithViewLifecycle {
            launch {
                scheduleViewModel.scheduleUiData.collect { updateScheduleUi(it) }
            }
        }
    }

    private fun updateScheduleUi(scheduleUiData: ScheduleUiData) {
        // Require everything to be loaded/
        val list = scheduleUiData.list ?: return
        val timeZoneId = scheduleUiData.timeZoneId ?: return
        val indexer = scheduleUiData.dayIndexer ?: return

        dayIndexer = indexer
        // Prevent building new indicators until we get scroll information.
        cachedBubbleRange = null
        if (indexer.days.isEmpty()) {
            // Special case: the results are empty, so we won't get valid scroll information.
            // Set a bogus range to and rebuild the day indicators.
            cachedBubbleRange = -1..-1
            rebuildDayIndicators()
        }

        sessionsAdapter.submitList(list)

        scheduleRecyclerView.run {
            // Recreate the decoration used for the sticky time headers
        }
    }

    private fun rebuildDayIndicators() {
        // cachedBubbleRange will get set once we have scroll information, so wait until then.
        val bubbleRange = cachedBubbleRange ?: return
        val indicators = if (dayIndexer.days.isEmpty()) {
            TimeUtils.ConferenceDays.map { day: ConferenceDay ->
                DayIndicator(day = day, enabled = false)
            }
        } else {
            dayIndexer.days.mapIndexed { index: Int, day: ConferenceDay ->
                DayIndicator(day = day, checked = index in bubbleRange)
            }
        }

        dayIndicatorAdapter.submitList(indicators)
        dayIndicatorItemDecoration.bubbleRange = bubbleRange
    }
}