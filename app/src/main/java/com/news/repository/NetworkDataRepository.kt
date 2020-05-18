package com.news.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.news.converter.Converter
import com.news.model.entity.News
import com.news.service.AppDownloadManager
import com.news.service.AppRetrofitService
import com.news.util.ErrorType
import com.news.util.RemoteErrorEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class NetworkDataRepository(
    private val appRetrofitService: AppRetrofitService,
    private val localDataRepository: LocalDataRepository,
    private val appDownloadManager: AppDownloadManager
) : RemoteErrorEmitter {
    companion object {
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"
    }

    fun fetchData(): MutableLiveData<Boolean> {
        val returnData = MutableLiveData<Boolean>()
        CoroutineScope(Dispatchers.IO).launch {
            val response = safeApiCall(this@NetworkDataRepository) {
                appRetrofitService.fetchData()
            }
            if (response != null && response.isSuccessful && response.body() != null) {
                localDataRepository.saveNewsPreviewData(Converter.rssToNewsEntities(response.body()!!))
                returnData.postValue(response.isSuccessful)
            } else {
                returnData.postValue(false)
            }

        }

        return returnData
    }

    fun downloadNewsBody(news: News): MutableLiveData<News?> {
        val returnData = MutableLiveData<News?>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val eventInfo: Document = Jsoup.connect(news.url).get()
                val info: Elements = eventInfo.select("span.article-body")
                news.allText = info.text()
                news.isSaved = true
                returnData.postValue(
                    appDownloadManager.downloadNewsImage(news)
                )

            } catch (ex: Exception) {
                returnData.postValue(null)
            }
        }

        return returnData
    }

    fun getNewBody(news: News): MutableLiveData<News> {
        val returnData = MutableLiveData<News>()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val eventInfo: Document = Jsoup.connect(news.url).get()
                val info: Elements = eventInfo.select("span.article-body")
                news.allText = info.text()
                returnData.postValue(
                    news
                )

            } catch (ex: Exception) {
                returnData.postValue(null)
            }
        }

        return returnData
    }


    suspend inline fun <T> safeApiCall(
        emitter: RemoteErrorEmitter,
        crossinline responseFunction: suspend () -> T
    ): T? {
        return try {
            val response = withContext(Dispatchers.IO) { responseFunction.invoke() }
            response
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.printStackTrace()
                Log.e("ApiSafeCalls", " Safe Call error: ${e.localizedMessage}", e.cause)
                when (e) {
                    is HttpException -> {
                        val body = e.response()?.errorBody()
                        emitter.onError(getErrorMessage(body))
                    }
                    is SocketTimeoutException -> emitter.onError(ErrorType.TIMEOUT)
                    is IOException -> emitter.onError(ErrorType.NETWORK)
                    else -> emitter.onError(ErrorType.UNKNOWN)
                }
            }
            null
        }
    }

    override fun onError(msg: String) {
        Log.e("ERROR!!!!", msg)
    }

    override fun onError(errorType: ErrorType) {
        Log.e("ERROR!!!!", errorType.name)
    }

    fun getErrorMessage(responseBody: ResponseBody?): String {
        return try {
            val jsonObject = JSONObject(responseBody!!.string())
            when {
                jsonObject.has(MESSAGE_KEY) -> jsonObject.getString(MESSAGE_KEY)
                jsonObject.has(ERROR_KEY) -> jsonObject.getString(ERROR_KEY)
                else -> "Something wrong happened"
            }
        } catch (e: Exception) {
            "Something wrong happened"
        }
    }


}