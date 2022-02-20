package com.info.droidkaigiapplication.data.source.room

import com.info.droidkaigiapplication.data.DroidKaigiApiService
import com.info.droidkaigiapplication.data.Result
import java.lang.Exception

class RoomWebDataSource: RoomDataSource {

    private val roomApiService by lazy {
        DroidKaigiApiService.createApiService<RoomApiService>()
    }

    override fun getRooms(): Result<List<RoomData>> {
        try {
            val response = roomApiService.getRooms().execute()
            if (response.isSuccessful) {
                val rooms = (response.body() as RoomResponse).rooms
                return Result.Succeed(rooms)
            }
            return Result.Failure(response.code(), response.message())
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.Error(e)
        }
    }
}