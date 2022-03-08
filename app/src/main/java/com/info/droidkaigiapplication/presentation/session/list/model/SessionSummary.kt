package com.info.droidkaigiapplication.presentation.session.list.model

import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.presentation.getDays


data class SessionSummary(val id: Int,
                          val title: String,
                          val description: String,
                          val startsAt: String,
                          val endsAt: String,
                          val day: Int,
                          val isServiceSession: Boolean,
                          val isPlenumSession: Boolean,
                          val speakerSummary: SpeakerSummary,
                          val categoryItems: List<Int>,
                          val questionAnswers: List<*>,
                          val room: Room,
                          val videoUrl: String,
                          val slideUrl: String) {
}

fun List<SessionData>.toSessionSummaryList(room: Room,
                                           speakerSummaryList: List<SpeakerSummary>): List<SessionSummary> {
    return this.map {
        val speakerId = it.speakers.first()
        val speakerSummary = speakerSummaryList.getSpeakerSummary(speakerId)
        it.toSessionSummary(room, speakerSummary)
    }
}

fun List<SessionData>.toSessionSummaryList(roomList: List<Room>,
                                           speakerSummaryList: List<SpeakerSummary>): List<SessionSummary> {
    return this.map {
        val room = roomList.getRoom(it.roomId)
        val speakerId = it.speakers.first()
        val speakerSummary = speakerSummaryList.getSpeakerSummary(speakerId)
        it.toSessionSummary(room, speakerSummary)
    }
}

private fun List<Room>.getRoom(roomId: Int): Room {
    return this.first { it.id == roomId }
}

private fun List<SpeakerSummary>.getSpeakerSummary(speakerId: String): SpeakerSummary {
    return this.first { it.id == speakerId }
}

private fun SessionData.toSessionSummary(room: Room,
                                         speakerSummary: SpeakerSummary): SessionSummary {
    return SessionSummary(
            id,
            title,
            description,
            startsAt,
            endsAt,
            getDays(startsAt),
            isServiceSession,
            isPlenumSession,
            speakerSummary,
            categoryItems,
            questionAnswers,
            room,
            videoUrl,
            slideUrl
    )
}