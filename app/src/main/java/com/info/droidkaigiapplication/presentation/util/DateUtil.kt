package com.info.droidkaigiapplication.presentation.util

import java.text.ParseException
import java.text.SimpleDateFormat


fun getDateString(dateString: String, format: String, revisionFormat: String): String {
    val replacedDateString = dateString.replace("T", " ")
    val inputFormat = SimpleDateFormat(format)
    val outputFormat = SimpleDateFormat(revisionFormat)
    return try {
        outputFormat.format(inputFormat.parse(replacedDateString))
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}