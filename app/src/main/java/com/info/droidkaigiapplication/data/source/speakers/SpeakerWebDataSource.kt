package com.info.droidkaigiapplication.data.source.speakers

import com.info.droidkaigiapplication.data.DroidKaigiApiService
import com.info.droidkaigiapplication.data.Result
import java.lang.Exception

class SpeakerWebDataSource: SpeakerDataSource {

    private val speakerApiService by lazy {
        DroidKaigiApiService.createApiService<SpeakerApiService>()
    }

    override fun getSpeakers(): Result<List<SpeakerData>> {
        try {
            val response = speakerApiService.getSpeakers().execute()
            if (response.isSuccessful) {
                val speakers = (response.body() as SpeakerResponse).speakers
                return Result.Succeed(speakers)
            }
            return Result.Failure(response.code(), response.message())
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }
}