package com.news.repository

import androidx.lifecycle.MutableLiveData
import com.news.db.AppDatabase
import com.news.model.entity.News
import com.news.service.AppDownloadManager
import com.news.service.AppSettingsService
import com.news.service.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class LocalDataRepository(
    private val notificationService: NotificationService,
    private val appDatabase: AppDatabase,
    private val appSettingsService: AppSettingsService

) : KoinComponent {

    private val appDownloadManager: AppDownloadManager by inject()

    fun saveNewsPreviewData(news: MutableList<News>) {
        val insertedInfo = appDatabase.newsDao().insertAll(news.toTypedArray())
        if (!appSettingsService.foreground()) {
            for (item in insertedInfo) {
                if (item.toInt() != -1) {
                    getNewsById(item.toInt())?.let { notificationService.createNotification(it) }
                }
            }
        }


    }

    fun saveNewsPreviewDataAsync(news: MutableList<News>) {
        CoroutineScope(Dispatchers.IO).launch {
            val insertedInfo = appDatabase.newsDao().insertAll(news.toTypedArray())
            if (!appSettingsService.foreground()) {
                for (item in insertedInfo) {
                    if (true || item.toInt() != -1) {
                        getNewsById(news[0].id.toInt())?.let {
                            notificationService.createNotification(
                                it
                            )
                        }
                    }
                }
            }
        }

    }


    fun getAllNews(): MutableLiveData<MutableList<News>> {
        val mutableLiveData = MutableLiveData<MutableList<News>>()
        CoroutineScope(Dispatchers.IO).launch {
            mutableLiveData.postValue(appDatabase.newsDao().getAll()?.toMutableList())
        }
        return mutableLiveData

    }

    fun getSavedNews(): MutableLiveData<MutableList<News>> {
        val mutableLiveData = MutableLiveData<MutableList<News>>()
        CoroutineScope(Dispatchers.IO).launch {
            mutableLiveData.postValue(appDatabase.newsDao().getSavedNews()?.toMutableList())
        }
        return mutableLiveData

    }

    fun getNewsById(id: Int): News? {
        return appDatabase.newsDao().getNewsById(id)
    }

    fun getNewsByIdAsync(id: Int): MutableLiveData<News>? {
        val mutableLiveData = MutableLiveData<News>()
        CoroutineScope(Dispatchers.IO).launch {
            mutableLiveData.postValue(appDatabase.newsDao().getNewsById(id))
        }
        return mutableLiveData
    }

    fun updateNewStatus(new: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            appDatabase.newsDao().updateNewStatus(if (new) 1 else 0)
        }
    }

    fun updateNewsStatus(news: News): News {
        val mutableLiveData = MutableLiveData<News>()
        appDatabase.newsDao()
            .updateNewsStatus(if (news.isSaved) 1 else 0, news.allText, news.savedImagePath, news.id)
        return appDatabase.newsDao().getNewsById(news.id)
    }


    fun deleteDownloadedData(news: News): MutableLiveData<News> {
        val mutableLiveData = MutableLiveData<News>()
        CoroutineScope(Dispatchers.IO).launch {
            appDownloadManager.deleteDownloadedImage(news)
            news.savedImagePath = null
            news.isSaved = false
            news.allText = null
            appDatabase.newsDao().updateNewsStatus(0, null, news.savedImagePath, news.id)
            mutableLiveData.postValue(appDatabase.newsDao().getNewsById(news.id))
        }

        return mutableLiveData
    }
}