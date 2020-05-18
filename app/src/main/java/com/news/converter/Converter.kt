package com.news.converter

import com.news.model.dto.Rss
import com.news.model.entity.News
import com.news.util.Utils
import java.text.SimpleDateFormat
import java.util.*

object Converter {

    fun rssToNewsEntities(rss: Rss): MutableList<News> {
        val formatter = SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z");
        val date = Date()
        val strDate = formatter.format(date);

        val returnData = mutableListOf<News>()
        for (item in rss.channel?.item!!) {
            try {
                val newsId = Utils.getNewsIdFromLink(item.link)
                if (newsId != -1) {

                    var pubDate = item.pubDate
                    try {
                        val date = SimpleDateFormat("E, d MMM ").format(Date(item.pubDate))
                        pubDate = date.toString()
                    }catch (ex : Exception){}

                    val rssItem = News(
                        newsId, item.link, Utils.generateNewsImageUrlFromLink(item.link),
                        true, false, pubDate, item.title, null, null
                    )
                    returnData.add(rssItem)

                }

            } catch (ex: Exception) {
            }
        }
        return returnData
    }
}