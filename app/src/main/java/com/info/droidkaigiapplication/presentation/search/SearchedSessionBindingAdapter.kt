package com.info.droidkaigiapplication.presentation.search

import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.info.droidkaigiapplication.presentation.search.model.SearchedSession
import com.info.droidkaigiapplication.presentation.session.detail.list.SessionDetailsActivity.Companion.start
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder


@BindingAdapter(value = ["context", "viewModel", "searchedSessions"])
fun setSearchedSessions(
        recyclerView: RecyclerView,
        context: Context,
        viewModel: SearchViewModel,
        searchedSessions: List<SearchedSession>?) {
    if (searchedSessions == null) {
        return
    }
    recyclerView.adapter?.run {
        recyclerView.scrollToPosition(0)
        recyclerView.update(searchedSessions)
    } ?: run {
        recyclerView.setSearchedSessionRecyclerView(context, viewModel)
        recyclerView.update(searchedSessions)
    }
}

private fun RecyclerView.setSearchedSessionRecyclerView(context: Context, viewModel: SearchViewModel) {
    val groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
        val searchedSessionSection = SearchedSessionSection(viewModel) { session ->
            context.start(session.id)
        }
        add(searchedSessionSection)
    }
    with(this) {
        adapter = groupAdapter
        addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }
}

private fun RecyclerView.update(searchedSessions: List<SearchedSession>) {
    val searchedSessionSection = (this.adapter as GroupAdapter).run {
        getTopLevelGroup(0) as SearchedSessionSection
    }
    searchedSessionSection.update(searchedSessions)
}