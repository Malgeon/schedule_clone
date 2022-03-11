package com.example.schedule_clone.domain.search

import com.example.schedule_clone.data.db.AppDatabase
import com.example.schedule_clone.model.userdata.UserSession
import javax.inject.Inject

interface SessionTextMatchStrategy {
    suspend fun searchSessions(userSessions: List<UserSession>, query: String): List<UserSession>
}

/** Searches sessions by simple string comparison against title and description. */
object SimpleMatchStrategy : SessionTextMatchStrategy {

    override suspend fun searchSessions(
        userSessions: List<UserSession>,
        query: String
    ): List<UserSession> {
        TODO("Not yet implemented")
    }
}

/** Searches sessions using FTS. */
class FtsMatchStrategy @Inject constructor(
    private val appDatabase: AppDatabase
) : SessionTextMatchStrategy {

    override suspend fun searchSessions(
        userSessions: List<UserSession>,
        query: String
    ): List<UserSession> {
        TODO("Not yet implemented")
    }
}