package com.mat.inspector

import android.app.Application
import android.util.Log
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {

        }
    }
}