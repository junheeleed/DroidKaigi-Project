package com.info.droidkaigiapplication.data.source.speakers

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface  SpeakerApiService {

    @GET("sessionize/all.json")
    fun getSpeakers(): Call<SpeakerResponse>
}

data class SpeakerResponse(
    @SerializedName("speakers") val speakers: List<SpeakerData>
)

data class SpeakerData(
        @SerializedName("id") val id: String,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("bio") val bio: String,
        @SerializedName("tagLine") val tagLine: String,
        @SerializedName("profilePicture") val profilePicture: String,
        @SerializedName("isTopSpeaker") val isTopSpeaker: Boolean,
        @SerializedName("links") val links: List<LinkData>,
        @SerializedName("sessions") val sessions: List<Int>,
        @SerializedName("fullName") val fullName: String
)

data class LinkData(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("linkType") val linkType: String
)