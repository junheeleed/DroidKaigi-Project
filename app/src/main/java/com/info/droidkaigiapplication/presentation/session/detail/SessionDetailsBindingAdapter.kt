package com.info.droidkaigiapplication.presentation.session.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.info.droidkaigiapplication.presentation.session.detail.model.SessionDetail


@BindingAdapter(value = ["appCompatActivity", "sessionDetails", "sessionId", "roomId"])
fun setSessionDetails(viewPager2: ViewPager2,
                      appCompatActivity: AppCompatActivity,
                      sessionDetails: List<SessionDetail>?,
                      sessionId: Int,
                      roomId: Int) {
    if (sessionDetails == null) {
        return
    }
    val sessionDetailAdapter = SessionDetailAdapter(appCompatActivity, roomId)
    sessionDetailAdapter.sessionDetails = sessionDetails
    viewPager2.adapter = sessionDetailAdapter
    viewPager2.currentItem = sessionDetails.indexOfFirst { it.id == sessionId }
}