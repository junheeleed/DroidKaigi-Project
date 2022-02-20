package com.info.droidkaigiapplication.presentation.session.list

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.droidkaigiapplication.presentation.pref.PreviousSessionPrefs
import com.info.droidkaigiapplication.presentation.restoreScrollState
import com.info.droidkaigiapplication.presentation.session.adapter.SessionsSection
import com.info.droidkaigiapplication.presentation.session.detail.SessionDetailsActivity.Companion.start
import com.info.droidkaigiapplication.presentation.session.model.Session
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


@BindingAdapter(value = ["sessions", "recyclerViewPool"])
fun setSessions(recyclerView: RecyclerView,
                sessions: List<Session>?,
                recyclerViewPool: RecyclerView.RecycledViewPool
) {
    if (sessions == null) {
        return
    }
    recyclerView.adapter?.run {
        updateAndScrollToPreviousSession(recyclerView, sessions)
    } ?: run {
        setSessionRecyclerView(recyclerView, recyclerViewPool)
        updateAndScrollToPreviousSession(recyclerView, sessions)
    }
}

private fun updateAndScrollToPreviousSession(
        recyclerView: RecyclerView,
        sessions: List<Session>) {
    ((recyclerView.adapter as GroupAdapter).getTopLevelGroup(0) as SessionsSection).update(sessions)
    scrollToPreviousSession(recyclerView)
}

private fun scrollToPreviousSession(recyclerView: RecyclerView) {
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    layoutManager.restoreScrollState(PreviousSessionPrefs.scrollState)
    PreviousSessionPrefs.initPreviousSessionPrefs()
}

private fun setSessionRecyclerView(recyclerView: RecyclerView,
                                   recyclerViewPool: RecyclerView.RecycledViewPool) {
    val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
        val sessionSection = SessionsSection {
            recyclerView.context.start(it.id, it.roomId)
        }
        add(sessionSection)
    }
    with(recyclerView) {
        adapter = groupAdapter
        addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(context, RecyclerView.VERTICAL))
        setRecycledViewPool(recyclerViewPool)
        (layoutManager as LinearLayoutManager).recycleChildrenOnDetach = true
    }
}