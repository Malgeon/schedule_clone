package com.example.schedule_clone.data.tag

import com.example.schedule_clone.data.ConferenceDataRepository
import com.example.schedule_clone.model.Tag
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single point of access to tag data for the presentation layer.
 */
@Singleton
open class TagRepository @Inject constructor(
    private val conferenceDataRepository: ConferenceDataRepository
) {
    fun getTags(): List<Tag> = conferenceDataRepository.getOfflineConferenceData().tags
}