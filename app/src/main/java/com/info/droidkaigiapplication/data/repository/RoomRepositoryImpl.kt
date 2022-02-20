package com.info.droidkaigiapplication.data.repository

import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.source.room.RoomData
import com.info.droidkaigiapplication.data.source.room.RoomDataSource

class RoomRepositoryImpl(
    private val roomDataSource: RoomDataSource)
    : RoomRepository {

    override suspend fun getRooms(): Result<List<RoomData>> {
        return roomDataSource.getRooms()
    }
}