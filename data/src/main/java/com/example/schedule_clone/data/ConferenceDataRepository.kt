package com.example.schedule_clone.data

import com.example.schedule_clone.data.db.AppDatabase
import com.example.schedule_clone.data.db.CodelabFtsEntity
import com.example.schedule_clone.data.db.SessionFtsEntity
import com.example.schedule_clone.data.db.SpeakerFtsEntity
import com.example.schedule_clone.model.ConferenceData
import com.example.schedule_clone.model.ConferenceDay
import com.example.schedule_clone.shared.util.TimeUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import java.io.IOException
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

    val currentConferenceDataVersion: Int
        get() = conferenceDataCache?.version ?: 0

    var latestException: Exception? = null
        private set
    var latestUpdateSource: UpdateSource = UpdateSource.NONE
        private set

    // Using a SharedFlow instead of StateFlow as there isn't an initial value to be emitted
    private val dataLastUpdatedFlow = MutableSharedFlow<Long>(replay = 1)
    val dataLastUpDatedObservable: Flow<Long> = dataLastUpdatedFlow

    // Prevents multiple consumers requesting data at the same time
    private val loadConfDataLock = Any()

    fun refreshCacheWithRemoteConferenceData() {
        val conferenceData = try {
            remoteDataSource.getRemoteConferenceData()
        } catch (e: IOException) {
            latestException = e
            throw e
        }
        if (conferenceData == null) {
            val e = Exception("Remote returned no conference data")
            latestException = e
            throw e
        }

        // Network data success!
        // Update cache
        synchronized(loadConfDataLock) {
            conferenceDataCache = conferenceData
            populateSearchData(conferenceData)
        }

        // Update meta
        latestException = null
        dataLastUpdatedFlow.tryEmit(System.currentTimeMillis())
        latestUpdateSource = UpdateSource.NETWORK
        latestException = null
    }

    fun getOfflineConferenceData(): ConferenceData {
        synchronized(loadConfDataLock) {
            val offlineData = conferenceDataCache ?: getCacheOrBootstrapDataAndPopulateSearch()
            conferenceDataCache = offlineData
            return offlineData
        }
    }

    private fun getCacheOrBootstrapDataAndPopulateSearch(): ConferenceData {
        val conferenceData = getCacheOrBootstrapData()
        populateSearchData(conferenceData)
        return conferenceData
    }

    private fun getCacheOrBootstrapData(): ConferenceData {
        // First, try the local cache:
        var conferenceData = remoteDataSource.getOfflineConferenceData()

        // Cache success!
        if (conferenceData != null) {
            latestUpdateSource = UpdateSource.CACHE
            return conferenceData
        }

        // Second, use the bootstrap file:
        conferenceData = boostrapDataSource.getOfflineConferenceData()!!
        latestUpdateSource = UpdateSource.BOOTSTRAP
        return conferenceData
    }

    open fun populateSearchData(conferenceData: ConferenceData) {
        Timber.e("data populate")
        val sessionFtsEntities = conferenceData.sessions.map { session ->
            SessionFtsEntity(
                sessionId = session.id,
                title = session.title,
                description = session.description,
                speakers = session.speakers.joinToString { it.name }
            )
        }
        appDatabase.sessionFtsDao().insertAll(sessionFtsEntities)
        val speakers = conferenceData.speakers.map {
            SpeakerFtsEntity(
                speakerId = it.id,
                name = it.name,
                description = it.biography
            )
        }
        appDatabase.speakerFtsDao().insertAll(speakers)
        val codelabs = conferenceData.codelabs.map {
            CodelabFtsEntity(
                codelabId = it.id,
                title = it.title,
                description = it.description
            )
        }
        appDatabase.codelabFtsDao().insertAll(codelabs)
    }

    open fun getConferenceDays(): List<ConferenceDay> = TimeUtils.ConferenceDays
}