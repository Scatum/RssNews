package com.news.app

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.news.DI.appManagers
import com.news.DI.appRepositories
import com.news.DI.appServices
import com.news.DI.appViewModels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
        registerProcessLifecycleOwner()
    }
    private fun registerProcessLifecycleOwner() {
        ProcessLifecycleOwner.get()
            .lifecycle
            .addObserver(AppStateListener())
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(appRepositories, appViewModels, appServices, appManagers))
        }
    }
}