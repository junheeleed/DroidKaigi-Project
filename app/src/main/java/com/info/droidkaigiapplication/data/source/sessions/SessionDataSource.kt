package com.info.droidkaigiapplication.data.source.sessions

import com.info.droidkaigiapplication.data.Result

interface SessionDataSource {
    fun getSessions(): Result<List<SessionData>>
}