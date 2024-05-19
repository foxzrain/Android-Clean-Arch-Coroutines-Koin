package com.android.post

import android.app.Application
import androidx.multidex.MultiDex
import com.android.post.di.ApiModule
import com.android.post.di.NetworkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(listOf(ApiModule, NetworkModule))
        }

    }
}