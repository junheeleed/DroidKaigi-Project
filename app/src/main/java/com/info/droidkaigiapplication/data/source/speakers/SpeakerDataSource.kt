package com.info.droidkaigiapplication.data.source.speakers

import com.info.droidkaigiapplication.data.Result

interface SpeakerDataSource {
    fun getSpeakers(): Result<List<SpeakerData>>
}