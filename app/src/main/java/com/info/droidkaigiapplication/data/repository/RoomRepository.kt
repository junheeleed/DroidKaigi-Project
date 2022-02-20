package com.info.droidkaigiapplication.data.repository

import com.info.droidkaigiapplication.data.Result
import com.info.droidkaigiapplication.data.source.room.RoomData

interface RoomRepository {
    suspend fun getRooms(): Result<List<RoomData>>
}