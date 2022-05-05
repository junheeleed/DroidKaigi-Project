package com.info.droidkaigiapplication.presentation.session.list

import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.droidkaigiapplication.presentation.pref.PreviousSessionPrefs
import com.info.droidkaigiapplication.presentation.restoreScrollState
import com.info.droidkaigiapplication.presentation.session.adapter.SessionsSection
import com.info.droidkaigiapplication.presentation.session.detail.list.SessionDetailsActivity.Companion.start
import com.info.droidkaigiapplication.presentation.session.list.model.SessionSummary
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


@BindingAdapter(value = ["context", "sessionSummaries", "recyclerViewPool"])
fun setSessions(recyclerView: RecyclerView,
                context: Context,
                sessionSummaries: List<SessionSummary>?,
                recyclerViewPool: RecyclerView.RecycledViewPool
) {
    if (sessionSummaries == null) {
        return
    }
    recyclerView.adapter?.run {
        updateAndScrollToPreviousSession(recyclerView, sessionSummaries)
    } ?: run {
        setSessionRecyclerView(context, recyclerView, recyclerViewPool)
        updateAndScrollToPreviousSession(recyclerView, sessionSummaries)
    }
}

private fun updateAndScrollToPreviousSession(
        recyclerView: RecyclerView,
        sessionSummaries: List<SessionSummary>) {
    ((recyclerView.adapter as GroupAdapter).getTopLevelGroup(0) as SessionsSection).update(sessionSummaries)
    scrollToPreviousSession(recyclerView)
}

private fun scrollToPreviousSession(recyclerView: RecyclerView) {
    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
    layoutManager.restoreScrollState(PreviousSessionPrefs.scrollState)
    PreviousSessionPrefs.initPreviousSessionPrefs()
}

private fun setSessionRecyclerView(context: Context,
                                   recyclerView: RecyclerView,
                                   recyclerViewPool: RecyclerView.RecycledViewPool) {
    val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
        val sessionSection = SessionsSection(context) {
            recyclerView.context.start(it.id, it.room.id)
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