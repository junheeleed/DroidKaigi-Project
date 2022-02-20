package com.info.droidkaigiapplication.presentation.binding

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.regex.Pattern


@BindingAdapter(value = ["dateStringWithDayOfWeek"])
fun TextView.dateStringWithDayOfWeek(dateString: String?) {
    if (dateString.isNullOrEmpty()) {
        text = ""
        return
    }
    val replacedDateString = dateString.replace("T", " ")
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일 (EEEE)\nHH시 mm분")
    try {
        text = outputFormat.format(inputFormat.parse(replacedDateString))
    } catch (e: ParseException) {
        e.printStackTrace()
        text = ""
    }
}

@BindingAdapter(value = ["dateString"])
fun TextView.dateString(dateString: String) {
    if (dateString.isNullOrEmpty()) {
        return
    }
    val replacedDateString = dateString.replace("T", " ")
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val outputFormat = SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분")
    try {
        text = outputFormat.format(inputFormat.parse(replacedDateString))
    } catch (e: ParseException) {
        e.printStackTrace()
        text = ""
    }
}

@BindingAdapter(value = ["textList"])
fun TextView.setTextList(strings: List<String>) {
    val stringBuilder = StringBuilder()
    for (string in strings) {
        stringBuilder.append("$string, ")
    }
    val appendString = stringBuilder.toString()
    text = if (appendString.isNotEmpty()) appendString.substring(0, appendString.lastIndexOf(",") - 1) else ""
}

@BindingAdapter(value = ["highLightText"])
fun TextView.setHighlightText(highLightText: String) {
    if (highLightText.isNullOrEmpty()) {
        return
    }

    val spannableStringBuilder = SpannableStringBuilder(text)
    val pattern = Pattern.compile(highLightText, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(text)

    while (matcher.find()) {
        spannableStringBuilder.setSpan(
                BackgroundColorSpan(Color.GREEN),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    text = spannableStringBuilder
}

@BindingAdapter(value = ["underLineString"])
fun TextView.setUnderLine(string: String) {
    val spannableString = SpannableString(string).apply {
        setSpan(UnderlineSpan(), 0, string.length, 0)
    }
    text = spannableString
}
