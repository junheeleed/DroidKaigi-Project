package com.info.droidkaigiapplication.presentation.pref

import com.chibatching.kotpref.KotprefModel
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.presentation.ScrollState

object PreviousSessionPrefs : KotprefModel() {
    override val kotprefName: String = "previous_session_prefs"
    var previousSessionTabIndex: Int by intPref(
            default = context.resources.getInteger(R.integer.pref_default_value_previous_session_tab_id)
    )
    private var previousSessionScrollPosition: Int by intPref(
            default = context.resources.getInteger(R.integer.pref_default_value_previous_session_scroll_position)
    )
    private var previousSessionScrollOffset: Int by intPref(
            default = context.resources.getInteger(R.integer.pref_default_value_previous_session_scroll_offset)
    )

    fun initPreviousSessionPrefs() {
        previousSessionTabIndex = context.resources.getInteger(
                R.integer.pref_default_value_previous_session_tab_id)
        previousSessionScrollPosition = context.resources.getInteger(
                R.integer.pref_default_value_previous_session_scroll_position)
        previousSessionScrollOffset = context.resources.getInteger(
                R.integer.pref_default_value_previous_session_scroll_offset)
    }

    var scrollState: ScrollState
        get() = ScrollState(
                anchorPosition = previousSessionScrollPosition,
                anchorOffset = previousSessionScrollOffset
        )
        set(scrollState) {
            previousSessionScrollPosition = scrollState.anchorPosition
            previousSessionScrollOffset = scrollState.anchorOffset
        }
}
