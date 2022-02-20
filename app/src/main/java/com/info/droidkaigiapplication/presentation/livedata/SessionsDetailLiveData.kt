package com.info.droidkaigiapplication.presentation.livedata

import androidx.lifecycle.MutableLiveData
import com.info.droidkaigiapplication.presentation.session.detail.model.SessionDetail

class SessionsDetailLiveData: MutableLiveData<List<SessionDetail>>() {

    private val list = mutableListOf<SessionDetail>()

    fun addAll(sessionDetails: List<SessionDetail>) {
        list.clear()
        list.addAll(sessionDetails)
        this.value = list
    }

    fun get(position: Int): SessionDetail = list[position]

    fun size(): Int = list.size
}