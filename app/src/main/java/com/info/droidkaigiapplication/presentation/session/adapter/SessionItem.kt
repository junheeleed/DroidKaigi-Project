package com.info.droidkaigiapplication.presentation.session.adapter

import android.view.View
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.ItemSessionBinding
import com.info.droidkaigiapplication.presentation.session.model.Session
import com.xwray.groupie.viewbinding.BindableItem

class SessionItem(
    private val session: Session,
    private val itemClickListener: (Session) -> Unit)
    : BindableItem<ItemSessionBinding>(session.hashCode().toLong()) {

    override fun bind(viewBinding: ItemSessionBinding, position: Int) {
        viewBinding.session = session
        viewBinding.root.setOnClickListener {
            itemClickListener(session)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_session
    }

    override fun initializeViewBinding(view: View): ItemSessionBinding {
        return ItemSessionBinding.bind(view)
    }
}