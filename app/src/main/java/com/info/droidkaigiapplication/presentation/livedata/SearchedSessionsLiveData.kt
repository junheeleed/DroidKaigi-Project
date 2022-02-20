package com.info.droidkaigiapplication.presentation.livedata

import androidx.lifecycle.MutableLiveData
import com.info.droidkaigiapplication.presentation.search.model.SearchedSession

class SearchedSessionsLiveData: MutableLiveData<List<SearchedSession>>() {

    private val list = mutableListOf<SearchedSession>()

    fun addAll(searchedSession: List<SearchedSession>) {
        list.clear()
        list.addAll(searchedSession)
        this.value = list
    }

    fun get(position: Int): SearchedSession = list[position]

    fun size(): Int = list.size
}