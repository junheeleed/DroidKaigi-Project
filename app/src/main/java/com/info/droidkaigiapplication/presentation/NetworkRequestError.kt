package com.info.droidkaigiapplication.presentation

import com.info.droidkaigiapplication.R
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class NetworkRequestError(val exception: Exception,
                               val messageResId: Int) {
    SOCKET_TIME_OUT(SocketTimeoutException(), R.string.network_not_connected),
    UNKNOWN_HOST(UnknownHostException(), R.string.network_not_connected),
    EXCEPTION(Exception(), R.string.data_load_failed)
}