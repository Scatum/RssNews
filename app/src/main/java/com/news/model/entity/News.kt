package com.news.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class News(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "is_new") var isNew: Boolean,
    @ColumnInfo(name = "is_saved") var isSaved: Boolean,
    @ColumnInfo(name = "pub_date") val pubDate: String?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "all_text") var allText: String?,
    @ColumnInfo(name = "saved_image_path") var savedImagePath: String?
)