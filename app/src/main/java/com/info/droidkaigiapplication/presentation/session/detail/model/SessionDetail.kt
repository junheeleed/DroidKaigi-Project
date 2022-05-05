package com.info.droidkaigiapplication.presentation.session.detail.model

import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.presentation.getDays
import com.info.droidkaigiapplication.presentation.session.Room


data class SessionDetail(val id: Int,
                         val title: String,
                         val description: String,
                         val startsAt: String,
                         val endsAt: String,
                         val day: Int,
                         val speakerDetail: SpeakerDetail,
                         val room: Room,
                         val isEmpty: Boolean = false) {

    companion object {
        fun empty() = SessionDetail(0, "", "", "", "", 0, SpeakerDetail(), Room(), true)
    }
}

fun SessionData.toSessionDetail(speakerDetail: SpeakerDetail,
                                room: Room): SessionDetail {
    return SessionDetail(
            id,
            title,
            description,
            startsAt,
            endsAt,
            getDays(startsAt),
            speakerDetail,
            room
    )
}