package com.example.schedule_clone.data

import com.example.schedule_clone.data.db.AppDatabase
import com.example.schedule_clone.model.ConferenceData
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * Single point of access to session data for the presentation layer.
 *
 * The session data is loaded from the bootstrap file.
 */
@Singleton
open class ConferenceDataRepository @Inject constructor(
    @Named("remoteConfDatasource") private val remoteDataSource: ConferenceDataSource,
    @Named("bootstrapConfDataSource") private val boostrapDataSource: ConferenceDataSource,
    private val appDatabase: AppDatabase
) {

    // In-memory cache of the conference data
    private var conferenceDataCache: ConferenceData? = null
}