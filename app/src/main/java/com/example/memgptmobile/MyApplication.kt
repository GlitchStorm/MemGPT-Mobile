package com.example.memgptmobile

import android.app.Application
import com.example.memgptmobile.debug.Logger
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val logDirectory = applicationContext.filesDir.path + File.separator + "logs"
        Logger.initialize(logDirectory)
    }
}
