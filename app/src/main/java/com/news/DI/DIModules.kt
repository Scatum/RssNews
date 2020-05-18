package com.news.DI

import androidx.work.WorkerParameters
import com.news.db.AppDatabase
import com.news.repository.LocalDataRepository
import com.news.repository.NetworkDataRepository
import com.news.service.*
import com.news.view_model.ArchiveViewModel
import com.news.view_model.DetailViewModel
import com.news.view_model.FeedViewModel
import com.news.view_model.SettingsViewModel
import com.news.worker.AppWorker
import com.news.worker.StartWorker
import org.koin.core.module.Module
import org.koin.dsl.module


val appRepositories: Module = module {
    single { NetworkDataRepository(get(), get(), get()) }
    single { LocalDataRepository(get(), get(), get()) }
}

val appManagers: Module = module {
    single { AppDownloadManager(get(), get(), get()) }
}

val appViewModels: Module = module {
    single { FeedViewModel(get(), get()) }
    single { ArchiveViewModel(get(), get()) }
    single { SettingsViewModel() }
    single { DetailViewModel(get(), get()) }
}


val appServices: Module = module {
    single { AppRetrofitService.create() }
    single { AppSettingsService() }
    single { AppDatabase.getDatabase(get()) }
    single { NotificationService(get()) }
    single { WorkerService() }
    single { PreferenceService(get()) }
}