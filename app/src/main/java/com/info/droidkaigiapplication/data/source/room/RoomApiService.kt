package com.info.droidkaigiapplication.data.source.room

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.GET

interface RoomApiService {

    @GET("sessionize/all.json")
    fun getRooms(): Call<RoomResponse>
}

data class RoomResponse(
    @SerializedName("rooms") val rooms: List<RoomData>
)

data class RoomData(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("sort") val sort: Int
)