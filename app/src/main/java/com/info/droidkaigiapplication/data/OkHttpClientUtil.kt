package com.info.droidkaigiapplication.data

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

fun getOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
    connectTimeout(10, TimeUnit.SECONDS)
    writeTimeout(10, TimeUnit.SECONDS)
    readTimeout(10, TimeUnit.SECONDS)
}.build()