package com.info.droidkaigiapplication.presentation.session.detail

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.info.droidkaigiapplication.presentation.session.detail.model.SessionDetail


@BindingAdapter(value = ["sessionDetail"])
fun TextView.setPeriodCha(sessionDetail: SessionDetail) {
    if ((sessionDetail.startsAt.isNotEmpty())
            and (sessionDetail.endsAt.isNotEmpty())) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }

}

@BindingAdapter(value = ["isEnabled"])
fun View.setEnable(isEnabled: Boolean) {
    this.isEnabled = isEnabled
}