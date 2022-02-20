package com.info.droidkaigiapplication.presentation.pref

import com.chibatching.kotpref.KotprefModel
import com.info.droidkaigiapplication.R

object Prefs : KotprefModel() {
    var enableReopenPreviousRoomSessions: Boolean by booleanPref(
            default = context.resources.getBoolean(R.bool.pref_default_value_enable_reopen_previous_room_sessions),
            key = R.string.pref_key_enable_reopen_previous_room_sessions
    )
}
