package com.info.droidkaigiapplication.presentation.search.suggestion

import androidx.appcompat.widget.SearchView

class OnSuggestionClickListener(
        private val suggestionClick: (position: Int) -> Unit
): SearchView.OnSuggestionListener {

    override fun onSuggestionSelect(position: Int): Boolean {
        return false
    }

    override fun onSuggestionClick(position: Int): Boolean {
        suggestionClick(position)
        return false
    }
}