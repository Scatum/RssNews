package com.news.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.news.model.entity.News
import com.news.repository.LocalDataRepository
import com.news.repository.NetworkDataRepository

class DetailViewModel(
    private val networkDataRepository: NetworkDataRepository,
    private val localDataRepository: LocalDataRepository
) : ViewModel() {

    fun downloadNewBody(news: News): MutableLiveData<News?> {
        return networkDataRepository.downloadNewsBody(news)
    }

    fun getNewsBodyData(news: News): MutableLiveData<News> {
        return networkDataRepository.getNewBody(news)
    }

    fun getNewsById(newsId:Int):MutableLiveData<News>?{
        return localDataRepository.getNewsByIdAsync(newsId)
    }

    fun deleteDownloadedData(news: News):MutableLiveData<News>{
        return localDataRepository.deleteDownloadedData(news)
    }

}