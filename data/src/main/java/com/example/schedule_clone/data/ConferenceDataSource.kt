package com.example.schedule_clone.data

import com.example.schedule_clone.model.ConferenceData

interface ConferenceDataSource {
    fun getRemoteConferenceData(): ConferenceData?
    fun getOfflineConferenceData(): ConferenceData?
}

enum class UpdateSource {
    NONE,
    NETWORK,
    CACHE,
    BOOTSTRAP
}