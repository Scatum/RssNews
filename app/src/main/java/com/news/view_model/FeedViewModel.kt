package com.news.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.news.model.entity.News
import com.news.repository.LocalDataRepository
import com.news.repository.NetworkDataRepository

class FeedViewModel(
    private val networkDataRepository: NetworkDataRepository,
    private val localDataRepository: LocalDataRepository
) : ViewModel() {

    fun fetchData():MutableLiveData<Boolean> {
        return networkDataRepository.fetchData()
    }

    fun getData():MutableLiveData<MutableList<News>>{
       return localDataRepository.getAllNews()
    }

    fun downloadNewsBody(news: News):MutableLiveData<News?>{
       return networkDataRepository.downloadNewsBody(news)
    }

    fun deleteDownloadedData(news: News):MutableLiveData<News>{
       return localDataRepository.deleteDownloadedData(news)
    }

    fun updateNewStatus(isNew: Boolean) {
        localDataRepository.updateNewStatus(isNew)
    }

    fun updateNewStatus(news: News) {
        localDataRepository.updateNewsStatus(news)
    }
}