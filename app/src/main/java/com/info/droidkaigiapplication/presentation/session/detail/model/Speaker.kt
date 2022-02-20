package com.info.droidkaigiapplication.presentation.session.detail.model

import com.info.droidkaigiapplication.data.source.speakers.LinkData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData

data class Speaker(val id: String = "",
                   val firstName: String = "",
                   val lastName: String = "",
                   val bio: String = "",
                   val tagLine: String = "",
                   val profilePicture: String = "",
                   val isTopSpeaker: Boolean = false,
                   val links: List<LinkData> = listOf(),
                   val sessions: List<Int> = listOf(),
                   val fullName: String = "")

fun List<SpeakerData>.toSpeakerList(): List<Speaker> {
    return this.map { it.toSpeaker() }
}

fun SpeakerData.toSpeaker(): Speaker {
    return Speaker(
            id,
            firstName,
            lastName,
            bio,
            tagLine,
            profilePicture,
            isTopSpeaker,
            links,
            sessions,
            fullName
    )
}