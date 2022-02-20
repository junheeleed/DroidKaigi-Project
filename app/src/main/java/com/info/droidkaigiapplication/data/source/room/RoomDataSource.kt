package com.info.droidkaigiapplication.data.source.room

import com.info.droidkaigiapplication.data.Result

interface RoomDataSource {
    fun getRooms(): Result<List<RoomData>>
}