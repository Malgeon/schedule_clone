package com.example.schedule_clone.domain.search

import com.example.schedule_clone.data.ConferenceDataRepository
import com.example.schedule_clone.data.tag.TagRepository
import com.example.schedule_clone.domain.UseCase
import com.example.schedule_clone.model.Tag
import com.example.schedule_clone.model.filters.Filter
import com.example.schedule_clone.shared.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

private val FILTER_CATEGORIES = listOf(
    Tag.CATEGORY_TYPE,
    Tag.CATEGORY_TOPIC,
    Tag.CATEGORY_LEVEL
)

/** Loads filters for the Search screen. */
class LoadSearchFiltersUseCase @Inject constructor(
    private val conferenceRepository: ConferenceDataRepository,
    private val tagRepository: TagRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher
) : UseCase<Unit, List<Filter>>(dispatcher) {

    override suspend fun execute(parameters: Unit): List<Filter> {
        val filters = mutableListOf<Filter>()
        filters.addAll(conferenceRepository.getConferenceDays().map { Filter.DateFilter(it) })
        filters.addAll(
            tagRepository.getTags()
                .filter { it.category in FILTER_CATEGORIES }
                .sortedWith(
                    compareBy({ FILTER_CATEGORIES.indexOf(it.category) }, { it.orderInCategory })
                )
                .map { Filter.TagFilter(it) }
        )
        return filters
    }
}