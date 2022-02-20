package com.info.droidkaigiapplication.data.repository

import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerDataSource

class SpeakerRepositoryImpl(
        private val speakerDataSource: SpeakerDataSource)
    : SpeakerRepository {

    override suspend fun getSpeakers(): Result<List<SpeakerData>> {
        return speakerDataSource.getSpeakers()
    }
}