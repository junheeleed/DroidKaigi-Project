package com.info.droidkaigiapplication.data.source.sessions

import com.info.droidkaigiapplication.data.DroidKaigiApiService
import com.info.droidkaigiapplication.data.Result
import java.lang.Exception

class SessionWebDataSource: SessionDataSource {

    private val sessionApiService by lazy {
        DroidKaigiApiService.createApiService<SessionApiService>()
    }

    override fun getSessions(): Result<List<SessionData>> {
        try {
            val response = sessionApiService.getSessions().execute()
            if (response.isSuccessful) {
                val sessionDataList = (response.body() as SessionResponse).sessions
                return Result.Succeed(sessionDataList)
            }
            return Result.Failure(response.code(), response.message())
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }
}
