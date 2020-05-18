package com.news.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.news.constant.NEWS_IMAGE_URL_FORMAT


object Utils {
    /**
     * params
     * link the news detail url
     *
     * return value
     * -1 if cannot get news id on negative case
     * news id on positive case
     *
     * */
    fun getNewsIdFromLink(link: String?): Int {
        return try {
            if (link == null) {
                return -1
            } else link.split("news/", ".html").get(1).toInt()
        } catch (ex: Exception) {
            -1
        }

    }


    /**
     * params
     * link the news detail url
     *
     * return value
     * empty string ("")  on negative case
     * news image url on positive case
     *
     * ***note there is no garanty that generated image url is exist
     *
     * */
    fun generateNewsImageUrlFromLink(link: String?): String? {
        return try {
            if (link == null) {
                return null
            } else {
                val newsId = getNewsIdFromLink(link)
                if (newsId != -1) {
                    return String.format(
                        NEWS_IMAGE_URL_FORMAT,
                        newsId.toString().replace("..(?!$)".toRegex(), "$0/")
                    )
                } else {
                    ""
                }
                ""
            }
        } catch (ex: Exception) {
            ""
        }

    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }
}