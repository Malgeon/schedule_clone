package com.example.schedule_clone.data.session

import com.example.schedule_clone.data.ConferenceDataRepository
import com.example.schedule_clone.model.ConferenceDay
import com.example.schedule_clone.model.Session
import com.example.schedule_clone.model.SessionId
import javax.inject.Inject

interface SessionRepository {
    fun getSessions(): List<Session>
    fun getSession(eventId: SessionId): Session
    fun getConferenceDays(): List<ConferenceDay>
}

class DefaultSessionRepository @Inject constructor(
    private val conferenceDataRepository: ConferenceDataRepository
) : SessionRepository {
    override fun getSessions(): List<Session> {
        TODO("Not yet implemented")
    }

    override fun getSession(eventId: SessionId): Session {
        TODO("Not yet implemented")
    }

    override fun getConferenceDays(): List<ConferenceDay> {
        TODO("Not yet implemented")
    }
}

class SessionNotFoundException : Throwable()