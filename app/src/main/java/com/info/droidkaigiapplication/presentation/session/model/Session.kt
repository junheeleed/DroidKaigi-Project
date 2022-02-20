package com.info.droidkaigiapplication.presentation.session.model

import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.presentation.getDays


data class Session(val id: Int = 0,
                   val title: String = "",
                   val description: String = "",
                   val startsAt: String = "",
                   val endsAt: String = "",
                   val day: Int = 0,
                   val isServiceSession: Boolean = false,
                   val isPlenumSession: Boolean = false,
                   val speakers: List<String> = listOf(),
                   val categoryItems: List<Int> = listOf(),
                   val questionAnswers: List<*> = listOf<Any>(),
                   val roomId: Int = 0,
                   val videoUrl: String? = "",
                   val slideUrl: String? = "") {
}

fun List<SessionData>.toSessionList(): List<Session> {
    return this.map { it.toSession() }
}

fun SessionData.toSession(): Session {
    return Session(
            id,
            title,
            description,
            startsAt,
            endsAt,
            getDays(startsAt),
            isServiceSession,
            isPlenumSession,
            speakers,
            categoryItems,
            questionAnswers,
            roomId,
            videoUrl,
            slideUrl
    )
}