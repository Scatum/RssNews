package com.news.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.news.model.entity.News
import com.news.repository.LocalDataRepository
import com.news.repository.NetworkDataRepository

class ArchiveViewModel(
    private val localDataRepository: LocalDataRepository,
    private val networkDataRepository: NetworkDataRepository
) : ViewModel() {

    fun getSavedNews(): MutableLiveData<MutableList<News>> {
        return localDataRepository.getSavedNews()
    }

    fun deleteDownloadedData(news: News): MutableLiveData<News> {
        return localDataRepository.deleteDownloadedData(news)
    }

    fun downloadNewBody(news: News): MutableLiveData<News?> {
        return networkDataRepository.downloadNewsBody(news)
    }

}