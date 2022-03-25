package com.example.schedule_clone.presentation.search

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.schedule_clone.presentation.R
import com.example.schedule_clone.presentation.databinding.FragmentSearchBinding
import com.example.schedule_clone.presentation.schedule.ScheduleTwoPaneViewModel
import com.example.schedule_clone.presentation.sessioncommon.SessionsAdapter
import com.example.schedule_clone.shared.analytics.AnalyticsHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    @field:Named("tagViewPool")
    lateinit var tagViewPool: RecyclerView.RecycledViewPool

    private lateinit var binding: FragmentSearchBinding

    private val viewModel: SearchViewModel by viewModels()
    private val scheduleTwoPaneViewModel: ScheduleTwoPaneViewModel by activityViewModels()

    private lateinit var sessionsAdapter: SessionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val themedInflater =
            inflater.cloneInContext(ContextThemeWrapper(requireContext(), R.style.AppTheme_Detail))
        binding = FragmentSearchBinding.inflate(themedInflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        binding.toolbar.apply {
            inflateMenu(R.menu.search_menu)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_open_filters) {
                    findFiltersFragment().showFiltersSheet()
                    true
                } else {
                    false
                }
            }
        }
    }


    private fun findFiltersFragment(): SearchFilterFragment {
        return childFragmentManager.findFragmentById(R.id.filter_sheet) as SearchFilterFragment
    }



}