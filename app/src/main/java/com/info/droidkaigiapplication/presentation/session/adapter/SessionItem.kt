package com.info.droidkaigiapplication.presentation.session.adapter

import android.content.Context
import android.view.View
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.ItemSessionBinding
import com.info.droidkaigiapplication.presentation.session.list.model.SessionSummary
import com.xwray.groupie.viewbinding.BindableItem

class SessionItem(
        private val context: Context,
        private val sessionSummary: SessionSummary,
        private val itemClickListener: (SessionSummary) -> Unit)
    : BindableItem<ItemSessionBinding>(sessionSummary.hashCode().toLong()) {

    override fun bind(viewBinding: ItemSessionBinding, position: Int) {
        viewBinding.context = context
        viewBinding.sessionSummary = sessionSummary
        viewBinding.root.setOnClickListener {
            itemClickListener(sessionSummary)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_session
    }

    override fun initializeViewBinding(view: View): ItemSessionBinding {
        return ItemSessionBinding.bind(view)
    }
}