package com.info.droidkaigiapplication.presentation.session.adapter

import android.view.View
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.ItemSessionHeaderBinding
import com.info.droidkaigiapplication.presentation.session.model.Session
import com.xwray.groupie.viewbinding.BindableItem

class SessionHeaderItem(
        private val session: Session)
    : BindableItem<ItemSessionHeaderBinding>(session.hashCode().toLong()) {

    override fun bind(viewBinding: ItemSessionHeaderBinding, position: Int) {
        viewBinding.session = session
    }

    override fun getLayout(): Int {
        return R.layout.item_session_header
    }

    override fun initializeViewBinding(view: View): ItemSessionHeaderBinding {
        return ItemSessionHeaderBinding.bind(view)
    }
}