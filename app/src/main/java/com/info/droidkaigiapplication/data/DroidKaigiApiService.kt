package com.info.droidkaigiapplication.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DroidKaigiApiService {

    companion object {
        inline fun <reified T> createApiService(): T {
            return Retrofit.Builder()
                    .baseUrl("https://droidkaigi.jp/2018/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create()).build()
                    .create(T::class.java)
        }
    }
}