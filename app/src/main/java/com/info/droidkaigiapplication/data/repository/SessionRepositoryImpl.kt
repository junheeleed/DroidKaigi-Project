package com.info.droidkaigiapplication.data.repository

import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.data.source.sessions.SessionDataSource

class SessionRepositoryImpl(
        private val sessionDataSource: SessionDataSource)
    : SessionRepository {

    override suspend fun getSessions(): Result<List<SessionData>> {
        return sessionDataSource.getSessions()
    }
}