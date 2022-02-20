package com.info.droidkaigiapplication.presentation.search.model

import com.info.droidkaigiapplication.data.source.sessions.SessionData
import com.info.droidkaigiapplication.data.source.speakers.SpeakerData
import com.info.droidkaigiapplication.presentation.intersect
import com.info.droidkaigiapplication.presentation.session.detail.model.Speaker
import com.info.droidkaigiapplication.presentation.session.detail.model.toSpeakerList

data class SearchedSession(
        val id: Int,
        val title: String,
        val description: String,
        val speakers: List<String>
)


fun List<SessionData>.toSearchedSessionList(speakerDataList: List<SpeakerData>): List<SearchedSession> {
    return this.map { it.toSearchedSession(speakerDataList) }
}

fun SessionData.toSearchedSession(speakerDataList: List<SpeakerData>): SearchedSession {
    return SearchedSession(
            id,
            title,
            description,
            speakerDataList.toSpeakerStringList(speakers)
    )
}

private fun List<SpeakerData>.toSpeakerStringList(speakerListOfSessionData: List<String>): List<String> {
    val list: List<Speaker> = intersect(speakerListOfSessionData, {
        speakerData: SpeakerData, s: String -> speakerData.id == s
    }).toSpeakerList()
    return list.map { speaker ->  speaker.fullName }
}