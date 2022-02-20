package com.info.droidkaigiapplication.presentation.search

import android.content.SearchRecentSuggestionsProvider
import com.info.droidkaigiapplication.BuildConfig

class SearchSuggestionProvider : SearchRecentSuggestionsProvider() {
    companion object {
        val AUTHORITY: String =
                "${BuildConfig.APPLICATION_ID}.presentation.search.SearchSuggestionProvider"
        val MODE: Int = DATABASE_MODE_QUERIES
    }

    init {
        this.setupSuggestions(AUTHORITY, MODE)
    }
}
