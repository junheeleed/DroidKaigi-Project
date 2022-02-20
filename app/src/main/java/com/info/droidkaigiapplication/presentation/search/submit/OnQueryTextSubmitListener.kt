package com.info.droidkaigiapplication.presentation.search.submit

import androidx.appcompat.widget.SearchView

class OnQueryTextSubmitListener(
        private val queryTextSubmit: (query: String?) -> Unit
): SearchView.OnQueryTextListener {

    override fun onQueryTextSubmit(query: String?): Boolean {
        queryTextSubmit(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}
