package com.info.droidkaigiapplication.data

import java.lang.Exception

sealed class Result<out T : Any> {
    data class Succeed<out T : Any>(val data: T): Result<T>()
    data class Failure(val code: Int, val message: String): Result<Nothing>()
    data class Error(val exception: Exception): Result<Nothing>()
}