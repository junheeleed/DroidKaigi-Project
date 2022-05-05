package com.info.droidkaigiapplication.presentation.session.detail.list

import com.info.droidkaigiapplication.data.source.sessions.SessionData


data class SessionDetailSummary(val id: Int,
                                val roomId: Int,
                                val title: String) {
}

fun List<SessionData>.toSessionDetailList(): List<SessionDetailSummary> {
    return this.map { it.toSessionDetail() }
}

fun SessionData.toSessionDetail(): SessionDetailSummary {
    return SessionDetailSummary(
            id,
            roomId,
            title
    )
}