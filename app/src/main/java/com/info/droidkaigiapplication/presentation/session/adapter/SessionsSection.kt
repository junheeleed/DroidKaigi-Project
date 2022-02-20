package com.info.droidkaigiapplication.presentation.session.adapter

import com.info.droidkaigiapplication.presentation.session.model.Session
import com.xwray.groupie.Item
import com.xwray.groupie.Section

class SessionsSection(
        private val itemClickListener: (session: Session) -> Unit
): Section() {

    fun update(sessions: List<Session>) {
        val itemList = mutableListOf<Item<*>>()
        val sessionMap: Map<String, List<Session>> = sessions.groupBy { it.startsAt }

        for ((_, sessionList) in sessionMap) {
            itemList.add(SessionHeaderItem(sessionList.first()))
            for (session in sessionList) {
                itemList.add(SessionItem(session, itemClickListener))
            }
        }
        update(itemList)
    }

}