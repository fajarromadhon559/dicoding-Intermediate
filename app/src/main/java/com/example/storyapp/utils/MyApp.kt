package com.example.storyapp.utils

import android.app.Application
import android.content.Context

class MyApp: Application() {

    companion object {
        var appContext: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}