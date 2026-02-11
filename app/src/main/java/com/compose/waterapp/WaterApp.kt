package com.compose.waterapp

import android.app.Application
import com.compose.waterapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WaterApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@WaterApp)
            modules(appModule)
        }
    }
}
