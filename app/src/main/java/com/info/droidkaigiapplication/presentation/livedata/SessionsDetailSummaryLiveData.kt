package com.info.droidkaigiapplication.presentation.livedata

import androidx.lifecycle.MutableLiveData
import com.info.droidkaigiapplication.presentation.session.detail.list.SessionDetailSummary

class SessionsDetailSummaryLiveData: MutableLiveData<List<SessionDetailSummary>>() {

    private val list = mutableListOf<SessionDetailSummary>()

    fun addAll(sessionDetailSummaries: List<SessionDetailSummary>) {
        list.clear()
        list.addAll(sessionDetailSummaries)
        this.value = list
    }

    fun get(position: Int): SessionDetailSummary = list[position]

    fun size(): Int = list.size
}