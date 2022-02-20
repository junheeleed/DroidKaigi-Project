package com.info.droidkaigiapplication.presentation

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.data.Result
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat


fun showAlertDialog(
        activity: Activity,
        title: String,
        message: String
) {
    val alertDialog = AlertDialog.Builder(activity).create().apply {
        setTitle(title)
        setMessage(message)
        setButton(AlertDialog.BUTTON_NEUTRAL, activity.applicationContext.getString(android.R.string.ok)) {
            dialog, _ -> dialog!!.dismiss()
        }
    }
    alertDialog.show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
}

fun getDays(dateString: String): Int {
    if (dateString.isEmpty()) {
        return 0
    }
    val replacedDateString = dateString.replace("T", " ")
    val nowTime = DateTime.now()
    val dateTime = DateTime.parse(replacedDateString, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
    return Days.daysBetween(nowTime, dateTime).days
}

fun <T, U> List<T>.intersect(uList: List<U>, filterPredicate: (T, U) -> Boolean): List<T> {
    return filter { t -> uList.any { u -> filterPredicate(t, u) } }
}

fun Context.openBrowser(urlString: String) {
    var url = urlString
    if (!urlString.startsWith("http://") and !urlString.startsWith("https://")) {
        url = "http://$urlString"
    }
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}

fun <T : Any> Result<T>.isFailed(): Boolean {
    return (this is Result.Failure) or (this is Result.Error)
}

fun <T : Any> Result<T>.getFailureMessage(context: Context): String {
    return NetworkRequestFailureMessage(context, this).getMessage()
}