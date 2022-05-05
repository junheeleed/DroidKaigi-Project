package com.info.droidkaigiapplication.presentation.search

import android.view.View
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.ItemSearchedSessionBinding
import com.info.droidkaigiapplication.presentation.search.model.SearchedSession
import com.xwray.groupie.viewbinding.BindableItem

class SearchedSessionItem(
        private val searchViewModel: SearchViewModel,
        private val searchedSession: SearchedSession,
        private val itemClickListener: (SearchedSession) -> Unit)
    : BindableItem<ItemSearchedSessionBinding>(searchedSession.hashCode().toLong()) {

    override fun bind(viewBinding: ItemSearchedSessionBinding, position: Int) {
        viewBinding.vm = searchViewModel
        viewBinding.searchedSession = searchedSession
        viewBinding.root.setOnClickListener {
            itemClickListener(searchedSession)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_searched_session
    }

    override fun initializeViewBinding(view: View): ItemSearchedSessionBinding {
        return ItemSearchedSessionBinding.bind(view)
    }
}