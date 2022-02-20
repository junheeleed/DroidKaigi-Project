package com.info.droidkaigiapplication.presentation.livedata

import androidx.lifecycle.MutableLiveData
import com.info.droidkaigiapplication.presentation.session.model.Session

class SessionsLiveData: MutableLiveData<List<Session>>() {

    private val list = mutableListOf<Session>()

    fun addAll(sessions: List<Session>) {
        list.clear()
        list.addAll(sessions)
        this.value = list
    }

    fun get(position: Int): Session = list[position]

    fun size(): Int = list.size
}