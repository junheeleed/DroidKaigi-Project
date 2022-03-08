package com.info.droidkaigiapplication.presentation.livedata

import androidx.lifecycle.MutableLiveData
import com.info.droidkaigiapplication.presentation.session.list.model.SessionSummary

class SessionSummariesLiveData: MutableLiveData<List<SessionSummary>>() {

    private val list = mutableListOf<SessionSummary>()

    fun addAll(sessionSummaries: List<SessionSummary>) {
        list.clear()
        list.addAll(sessionSummaries)
        this.value = list
    }

    fun get(position: Int): SessionSummary = list[position]

    fun size(): Int = list.size
}