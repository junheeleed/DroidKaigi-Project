package com.info.droidkaigiapplication.presentation.livedata

import androidx.lifecycle.LiveData
import com.info.droidkaigiapplication.presentation.session.list.model.Room

class RoomsLiveData: LiveData<List<Room>>() {

    private val list = mutableListOf<Room>()

    fun addAll(rooms: List<Room>) {
        list.clear()
        list.addAll(rooms)
        this.value = list
    }

    fun getList(): List<Room> = list

    fun size () = list.size
}