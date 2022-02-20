package com.info.droidkaigiapplication.presentation

import android.content.Context
import com.info.droidkaigiapplication.R
import com.info.droidkaigiapplication.data.Result

class NetworkRequestFailureMessage<out T : Any>(
        private val context: Context,
        private val result: Result<T>) {

    fun getMessage(): String {
        if (result is Result.Failure) {
            return context.getString(R.string.data_load_failed)
        }
        return (result as Result.Error).getErrorMessage()
    }

    private fun Result.Error.getErrorMessage(): String {
        val messageResId = when(this.exception::class) {
            NetworkRequestError.SOCKET_TIME_OUT.exception::class,
            NetworkRequestError.UNKNOWN_HOST.exception::class -> {
                NetworkRequestError.SOCKET_TIME_OUT.messageResId
            }
            else -> {
                NetworkRequestError.EXCEPTION.messageResId
            }
        }
        return context.getString(messageResId)
    }
}