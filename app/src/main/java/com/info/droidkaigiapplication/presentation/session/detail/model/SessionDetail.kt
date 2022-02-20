package com.info.droidkaigiapplication.presentation.session.detail.model

import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.presentation.getDays
import com.info.droidkaigiapplication.presentation.session.model.Session


data class SessionDetail(val id: Int = 0,
                   val title: String = "",
                   val description: String = "",
                   val startsAt: String = "",
                   val endsAt: String = "",
                   val day: Int = 0,
                   val speakers: List<String> = listOf(),
                   val roomId: Int = 0,
                   val isEmpty: Boolean = false) {

    companion object {
        fun empty() = SessionDetail(isEmpty = true)
    }
}

fun List<SessionData>.toSessionDetailList(): List<SessionDetail> {
    return this.map { it.toSessionDetail() }
}

fun SessionData.toSessionDetail(): SessionDetail {
    return SessionDetail(
            id,
            title,
            description,
            startsAt,
            endsAt,
            getDays(startsAt),
            speakers,
            roomId
    )
}

fun Session.toSessionDetail(speakers: List<String>): SessionDetail {
    return SessionDetail(
            id,
            title,
            description,
            startsAt,
            endsAt,
            getDays(startsAt),
            speakers,
            roomId
    )
}