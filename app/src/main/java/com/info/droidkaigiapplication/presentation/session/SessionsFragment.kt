package com.info.droidkaigiapplication.presentation.session

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.databinding.FragmentSessionsBinding
import com.info.droidkaigiapplication.presentation.DataBindingFragment
import com.info.droidkaigiapplication.presentation.pref.Prefs
import com.info.droidkaigiapplication.presentation.pref.PreviousSessionPrefs
import com.info.droidkaigiapplication.presentation.session.list.AllSessionListFragment
import com.info.droidkaigiapplication.presentation.session.list.SessionListFragment
import com.info.droidkaigiapplication.presentation.session.model.Room
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel

class SessionsFragment
    : DataBindingFragment<FragmentSessionsBinding>() {

    private val viewModel: SessionsViewModel by viewModel()

    override fun getLayoutResId() = R.layout.fragment_sessions

    override fun setDataBinding(dataBinding: FragmentSessionsBinding) {
        with(dataBinding) {
            vm = viewModel
            lifecycleOwner = this@SessionsFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        EventBus.getDefault().register(this)
        dataBinding.savedInstanceStateBundle = savedInstanceState
        dataBinding.viewpager.adapter = SessionAdapter(this)
        setActionBar()
        setObservers()
        viewModel.loadRooms()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setActionBar() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(dataBinding.toolbar)
        dataBinding.toolbar.title = getString(R.string.app_name)
    }

    private fun setObservers() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
            dataBinding.progressbar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun onPause() {
        super.onPause()
        when (Prefs.enableReopenPreviousRoomSessions) {
            true -> saveCurrentSession()
            false -> PreviousSessionPrefs.initPreviousSessionPrefs()
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onSessionListEvent(sessionListLoadEvent: SessionListLoadEvent) {
        val isLoading = sessionListLoadEvent.isLoading
        if (isLoading) {
            dataBinding.progressbar.visibility = View.VISIBLE
        } else {
            dataBinding.progressbar.visibility = View.GONE
        }
    }

    private fun saveCurrentSession() {
        val sessionAdapter = dataBinding.viewpager.adapter as SessionAdapter
        val currentItem = dataBinding.viewpager.currentItem
        if (sessionAdapter.itemCount <= currentItem) return

        PreviousSessionPrefs.previousSessionTabIndex = currentItem
        val fragment = childFragmentManager.findFragmentByTag("f$currentItem")

        if (fragment is SavePreviousSessionScroller) {
            fragment.requestSavingScrollState()
        }
    }

    interface SavePreviousSessionScroller {
        fun requestSavingScrollState()
    }

    companion object {
        fun newInstance() = SessionsFragment()
    }
}

class SessionAdapter(
        private val fragment: Fragment)
    : FragmentStateAdapter(fragment) {

    private val tabList: List<Tab> = mutableListOf()

    sealed class Tab(
            val title: String)
    {
        abstract val fragment: Fragment
        abstract val className: String

        object ALL: Tab("All") {
            override val fragment: Fragment
                get() = AllSessionListFragment.newInstance()
            override val className: String
                get() = AllSessionListFragment::class.java.simpleName
        }

        data class ROOM(
                val room: Room)
            : Tab(room.name) {
            override val fragment: Fragment
                get() = SessionListFragment.newInstance(room.id)
            override val className: String
                get() = SessionListFragment::class.java.simpleName
        }
    }

    override fun getItemCount(): Int {
        return tabList.size
    }

    override fun createFragment(position: Int): Fragment {
        val usedFragment = fragment.childFragmentManager.findFragmentByTag("f$position")
        if (usedFragment != null) {
            return usedFragment
        }
        return (tabList[position]).fragment
    }

    fun setTab(roomList: List<Room>) {
        val list = arrayListOf<Tab>()
        list.add(Tab.ALL)
        for (room in roomList) {
            list.add(Tab.ROOM(room))
        }
        (tabList as ArrayList).apply {
            this.clear()
            this.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun getTabList() = tabList
}