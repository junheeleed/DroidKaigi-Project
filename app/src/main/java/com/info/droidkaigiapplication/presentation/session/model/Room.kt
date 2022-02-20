package com.info.droidkaigiapplication.presentation.session.model

import com.info.droidkaigiapplication.data.source.room.RoomData


data class Room(val id: Int,
           val name: String,
           val sort: Int) {
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