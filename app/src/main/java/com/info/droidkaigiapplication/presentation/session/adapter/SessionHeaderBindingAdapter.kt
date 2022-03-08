package com.info.droidkaigiapplication.presentation.session.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.info.droidkaigiapplication.presentation.util.getDateString

@BindingAdapter(value = ["startDate", "endDate"])
fun TextView.setSessionHeaderDate(startDate: String?, endDate: String?) {
    if (startDate.isNullOrEmpty() or endDate.isNullOrEmpty()) {
        text = ""
        return
    }

    text = getHeaderDateString(startDate!!, endDate!!)
}

private fun getHeaderDateString(startDate: String, endDate: String): String {
    val format = "yyyy-MM-dd HH:mm:ss"
    val startTime = getDateString(startDate, format, "HH:mm")
    val startDateWithNameOfDay = getDateString(startDate, format, "MM-dd (EEEE)")
    val endTime = getDateString(endDate, format, "HH:mm")
    return "$startTime - $endTime $startDateWithNameOfDay"
}