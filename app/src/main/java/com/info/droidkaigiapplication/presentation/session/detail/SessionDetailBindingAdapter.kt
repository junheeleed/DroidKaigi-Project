package com.info.droidkaigiapplication.presentation.session.detail

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.info.droidkaigiapplication.presentation.util.getDateString

@BindingAdapter(value = ["isEnabled"])
fun View.setEnable(isEnabled: Boolean) {
    this.isEnabled = isEnabled
}

@BindingAdapter(value = ["startDate", "endDate"])
fun TextView.setTime(startDate: String, endDate: String) {
    val format = "yyyy-MM-dd HH:mm:ss"
    val revisionFormat = "HH:mm"
    val startTime = getDateString(startDate, format, revisionFormat)
    val endTime = getDateString(endDate, format, revisionFormat)
    text = "$startTime - $endTime"
}

@BindingAdapter(value = ["errorMessage"])
fun View.setVisibility(errorMessage: String) {
    if (errorMessage.isNotEmpty()) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}