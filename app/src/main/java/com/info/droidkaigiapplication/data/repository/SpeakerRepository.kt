package com.info.droidkaigiapplication.data.repository

import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData

interface SpeakerRepository {
    suspend fun getSpeakers(): Result<List<SpeakerData>>
}