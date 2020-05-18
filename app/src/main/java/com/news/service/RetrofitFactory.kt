package com.news.service

import com.news.BuildConfig
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.KoinComponent
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


object RetrofitFactory : KoinComponent {


    fun makeRetrofitService(): AppRetrofitService {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val apiClient =
            OkHttpClient.Builder().addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request = chain.request().newBuilder()
                        .build()//can be added some headers here, for news.am we don't have any header
                    chain.proceed(request)
                }).addInterceptor(logging).readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(apiClient)
            .addConverterFactory(TikXmlConverterFactory.create())

            .build().create(AppRetrofitService::class.java)
    }

}