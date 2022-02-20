package com.info.droidkaigiapplication.presentation.search

import com.info.droidkaigiapplication.presentation.search.model.SearchedSession
import com.xwray.groupie.Section

class SearchedSessionSection(
        private val searchViewModel: SearchViewModel,
        private val itemClickListener: (searchedSession: SearchedSession) -> Unit
): Section() {

    fun update(searchedSessions: List<SearchedSession>) {
        val searchedSessionItems = searchedSessions.toSearchedSessionItemList()
        update(searchedSessionItems)
    }

    private fun List<SearchedSession>.toSearchedSessionItemList(): List<SearchedSessionItem> {
        return this.map {
            SearchedSessionItem(searchViewModel, it) { searchedSession ->
                itemClickListener(searchedSession)
            }
        }
    }
}