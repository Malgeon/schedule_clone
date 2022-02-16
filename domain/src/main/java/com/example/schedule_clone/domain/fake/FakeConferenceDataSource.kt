package com.example.schedule_clone.domain.fake

import com.example.schedule_clone.data.ConferenceDataSource
import com.example.schedule_clone.model.ConferenceData
import com.example.schedule_clone.shared.util.TimeUtils.ConferenceDays

object FakeConferenceDataSource : ConferenceDataSource{

    override fun getRemoteConferenceData() = getOfflineConferenceData()

    override fun getOfflineConferenceData(): ConferenceData? {
        val bootstrapContent = BootstrapConferenceDataSource.getOfflineConferenceData()

        return transformDataForStaging(bootstrapContent ?: throw Exception("Couldn't load data"))
    }

    private fun transformDataForStaging(data: ConferenceData): ConferenceData {
        val sessions = data.sessions.toMutableList()
        val speakers = data.speakers.toMutableList()
        val codelabs = data.codelabs.toMutableList()
        val tags = data.tags.toMutableList()

        // Rename the first session of each day
        ConferenceDays.forEachIndexed dayerdafdrdddd@{

        }
    }
}