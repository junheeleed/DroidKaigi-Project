package com.info.droidkaigiapplication.presentation.session.list.model

import com.info.droidkaigiapplication.data.source.speakers.SpeakerData

data class SpeakerSummary(val id: String,
                          val profilePicture: String,
                          val fullName: String)

fun List<SpeakerData>.toSpeakerSummaryList(): List<SpeakerSummary> {
    return this.map { it.toSpeakerSummary() }
}

fun SpeakerData.toSpeakerSummary(): SpeakerSummary {
    return SpeakerSummary(
            id,
            profilePicture,
            fullName
    )
}