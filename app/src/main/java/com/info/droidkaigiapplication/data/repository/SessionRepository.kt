package com.info.droidkaigiapplication.data.repository

import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.source.sessions.SessionData

interface SessionRepository {
    suspend fun getSessions(): Result<List<SessionData>>
}