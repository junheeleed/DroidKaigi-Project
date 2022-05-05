package com.info.droidkaigiapplication.presentation.session.detail.list

import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2


@BindingAdapter(value = ["appCompatActivity", "sessionDetailSummaries", "sessionId", "roomId"])
fun setSessionDetails(viewPager2: ViewPager2,
                      appCompatActivity: AppCompatActivity,
                      sessionDetailSummaries: List<SessionDetailSummary>?,
                      sessionId: Int,
                      roomId: Int) {
    if (sessionDetailSummaries == null) {
        return
    }
    val sessionDetailAdapter = SessionDetailAdapter(appCompatActivity, roomId)
    sessionDetailAdapter.sessionDetailSummaries = sessionDetailSummaries
    viewPager2.adapter = sessionDetailAdapter
    viewPager2.currentItem = sessionDetailSummaries.indexOfFirst { it.id == sessionId }
}