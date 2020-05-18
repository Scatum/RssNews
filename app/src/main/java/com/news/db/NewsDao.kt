package com.news.db

import androidx.room.*
import com.news.model.entity.News

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    fun getAll(): List<News>

    @Query("SELECT * FROM News WHERE is_saved = 1")
    fun getSavedNews(): List<News>

    @Query("SELECT * FROM news WHERE id IN (:newsIds)")
    fun loadAllByIds(newsIds: IntArray): List<News>

    /*  @Query("SELECT * FROM news WHERE url =url")
      fun findByUrl(url: String): News*/

    /**
     *
     * return value
     * if long value is -1 then item is not new
     * if long value is news id then we sure that news has
     * just inserted and we must show notification for it
     * */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(news: Array<News>): List<Long>

    @Query("SELECT * FROM News WHERE id = :id")
    fun getNewsById(id: Int): News

    @Query("UPDATE News SET is_new = :value WHERE is_new = 1")
    fun updateNewStatus(value: Int)

    @Query("UPDATE News SET is_saved = :isSaved, all_text= :allText,saved_image_path =:savedImagePath WHERE id = :newsId")
    fun updateNewsStatus(isSaved: Int, allText: String?, savedImagePath: String?, newsId: Int)

    @Delete
    fun delete(user: News)
}