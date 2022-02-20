package com.info.droidkaigiapplication.data.source.sessions

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface SessionApiService {

    @GET("sessionize/all.json")
    fun getSessions(): Call<SessionResponse>
}

data class SessionResponse(
    @SerializedName("sessions") val sessions: List<SessionData>
)

data class SessionData(
        @SerializedName("id") val id: Int = 0,
        @SerializedName("title") val title: String = "",
        @SerializedName("description") val description: String = "",
        @SerializedName("startsAt") val startsAt: String = "",
        @SerializedName("endsAt") val endsAt: String = "",
        @SerializedName("isServiceSession") val isServiceSession: Boolean = false,
        @SerializedName("isPlenumSession") val isPlenumSession: Boolean = false,
        @SerializedName("speakers") val speakers: List<String> = listOf(),
        @SerializedName("categoryItems") val categoryItems: List<Int> = listOf(),
        @SerializedName("questionAnswers") val questionAnswers: List<*> = listOf<Any>(),
        @SerializedName("roomId") val roomId: Int = 0,
        @SerializedName("videoUrl") val videoUrl: String = "",
        @SerializedName("slideUrl") val slideUrl: String = ""
)