package com.info.droidkaigiapplication.presentation.session

import com.info.droidkaigiapplication.data.source.room.RoomData


data class Room(val id: Int = 0,
                val name: String = "",
                val sort: Int = 0) {
}

fun List<RoomData>.toRoomList(): List<Room> {
    return this.map { it.toRoom() }
}

fun RoomData.toRoom(): Room {
    return Room(
            id,
            name,
            sort
    )
}