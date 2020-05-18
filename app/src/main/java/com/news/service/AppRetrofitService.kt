package com.news.service

import com.news.BuildConfig
import com.news.model.dto.Rss
import retrofit2.Response
import retrofit2.http.GET


interface AppRetrofitService {

    @GET(BuildConfig.RSS_URL)
    suspend fun fetchData(): Response<Rss>

    companion object {
        fun create(): AppRetrofitService {
            return RetrofitFactory.makeRetrofitService()
        }
    }
}