package com.info.droidkaigiapplication.presentation.session.adapter

import android.content.Context
import com.info.droidkaigiapplication.presentation.session.list.model.SessionSummary
import com.xwray.groupie.Item
import com.xwray.groupie.Section

class SessionsSection(
        private val context: Context,
        private val itemClickListener: (sessionSummary: SessionSummary) -> Unit
): Section() {

    fun update(sessionSummaryList: List<SessionSummary>) {
        val itemList = mutableListOf<Item<*>>()
        val sessionMap: Map<String, List<SessionSummary>> = sessionSummaryList.groupBy { it.startsAt }

        for ((_, sessionSummaries) in sessionMap) {
            itemList.add(SessionHeaderItem(sessionSummaries.first()))
            for (sessionSummary in sessionSummaries) {
                itemList.add(SessionItem(context, sessionSummary, itemClickListener))
            }
        }
        update(itemList)
    }
}