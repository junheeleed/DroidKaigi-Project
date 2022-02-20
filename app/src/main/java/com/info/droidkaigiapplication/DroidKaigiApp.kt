package com.info.droidkaigiapplication

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.chibatching.kotpref.gsonpref.gson
import com.google.gson.Gson
import com.info.droidkaigiapplication.di.dataSourceModule
import com.info.droidkaigiapplication.di.repositoryModule
import com.info.droidkaigiapplication.di.sessionRecyclerViewPool
import com.info.droidkaigiapplication.di.viewModelModule
import com.info.droidkaigiapplication.log.TimberDebugTree
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class DroidKaigiApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initJoda()
        initKotpref()
    }

    private fun initKoin() {
        startKoin {
            androidContext(applicationContext)
            androidLogger()
            modules(listOf(sessionRecyclerViewPool, viewModelModule, repositoryModule, dataSourceModule))
        }
    }

    private fun initJoda() {
        JodaTimeAndroid.init(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(TimberDebugTree())
        }
    }

    private fun initKotpref() {
        Kotpref.init(this)
        Kotpref.gson = Gson()
    }
}