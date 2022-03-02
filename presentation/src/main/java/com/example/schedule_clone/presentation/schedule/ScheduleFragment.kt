package com.example.schedule_clone.presentation.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule_clone.domain.sessions.ConferenceDayIndexer
import com.example.schedule_clone.model.ConferenceDay
import com.example.schedule_clone.presentation.databinding.FragmentScheduleBinding
import com.example.schedule_clone.presentation.util.launchAndRepeatWithViewLifecycle
import com.example.schedule_clone.presentation.widget.FadingSnackbar
import com.example.schedule_clone.shared.util.TimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ScheduleFragment : Fragment() {

    companion object {
        private const val DIALOG_NEED_TO_SIGN_IN = "dialog_need_to_sign_in"
        private const val DIALOG_CONFIRM_SIGN_OUT = "dialog_need_to_sign_in"
        private const val DIALOG_SCHEDULE_HINTS = "dialog_need_to_sign_in"
    }

    private val scheduleViewModel: ScheduleViewModel by viewModels()

    private lateinit var snackbar: FadingSnackbar

    private lateinit var scheduleRecyclerView: RecyclerView

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

        launchAndRepeatWithViewLifecycle {
            launch {
                scheduleViewModel.scheduleUiData.collect { updateScheduleUi(it)}
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
        dayIndicatorItemDecoration
    }
}