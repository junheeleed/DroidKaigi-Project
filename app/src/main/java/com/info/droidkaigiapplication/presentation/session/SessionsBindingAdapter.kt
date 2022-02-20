package com.info.droidkaigiapplication.presentation.session

import android.os.Bundle
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.info.droidkaigiapplication.presentation.pref.Prefs
import com.info.droidkaigiapplication.presentation.pref.PreviousSessionPrefs
import com.info.droidkaigiapplication.presentation.session.model.Room


@BindingAdapter(value = ["viewPager2"])
fun setSessionTabLayout(tabLayout: TabLayout,
                 viewPager2: ViewPager2) {
    val sessionAdapter = viewPager2.adapter as SessionAdapter
    tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
    TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
        tab.text = sessionAdapter.getTabList()[position].title
    }.attach()
}

@BindingAdapter(value = ["rooms", "savedInstanceState"])
fun setSessionViewPager(viewPager2: ViewPager2,
                        rooms: List<Room>?,
                        savedInstanceState: Bundle?) {
    if (rooms == null) {
        return
    }
    (viewPager2.adapter as SessionAdapter).setTab(rooms)

    if (Prefs.enableReopenPreviousRoomSessions and
            (savedInstanceState == null)) {
        viewPager2.reopenPreviousOpenedItem()
    }
}

private fun ViewPager2.reopenPreviousOpenedItem() {
    val sessionAdapter = this.adapter as SessionAdapter
    val previousItem = PreviousSessionPrefs.previousSessionTabIndex
    if (previousItem < 0) return
    if (sessionAdapter.itemCount <= previousItem) return
    this.currentItem = previousItem
}