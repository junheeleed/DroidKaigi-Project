package com.info.droidkaigiapplication.presentation.session.detail.model

import com.info.droidkaigiapplication.data.source.speakers.SpeakerData

data class SpeakerDetail(val id: String = "",
                          val profilePicture: String = "",
                          val fullName: String = "")

fun SpeakerData.toSpeakerDetail(): SpeakerDetail {
    return SpeakerDetail(
            id,
            profilePicture,
            fullName
    )
}