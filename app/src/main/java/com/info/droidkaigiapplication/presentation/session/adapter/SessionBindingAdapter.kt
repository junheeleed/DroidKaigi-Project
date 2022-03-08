package com.info.droidkaigiapplication.presentation.session.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.presentation.util.getDateString

@BindingAdapter(value = ["startDate"])
fun TextView.setSessionTime(startDate: String) {
    if (startDate.isNullOrEmpty()) {
        text = ""
        return
    }
    val format = "yyyy-MM-dd HH:mm:ss"
    text = getDateString(startDate, format, "HH:mm")
}

@BindingAdapter(value = ["context", "profilePictureUrl"])
fun ImageView.setProfilePicture(context: Context, profilePictureUrl: String) {
    Glide.with(context)
            .load(profilePictureUrl)
            .placeholder(R.drawable.profile_default)
            .circleCrop()
            .into(this)
}