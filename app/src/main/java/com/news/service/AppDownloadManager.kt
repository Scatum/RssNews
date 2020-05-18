package com.news.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.news.model.entity.News
import com.news.repository.LocalDataRepository
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class AppDownloadManager(
    private val context: Context,
    private val notificationService: NotificationService,
    private val localDataRepository: LocalDataRepository
) {


    suspend fun downloadNewsImage(
        news: News
    ): News? {

        val url: URL = URL(news.image)
        var connection: HttpURLConnection? = null
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            val downloadedBitmap = BitmapFactory.decodeStream(bufferedInputStream)
            saveTempBitmap(downloadedBitmap, news)
            news.savedImagePath = getImage(news)
            localDataRepository.updateNewsStatus(news)
            notificationService.createNotification(news, true)
            return localDataRepository.getNewsById(news.id)

        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }


    }

    private fun saveTempBitmap(bitmap: Bitmap?, news: News) {
        if (isExternalStorageWritable()) {
            bitmap?.let { saveImage(it, news) }
        } else {
            //prompt the user or do something
        }
    }

    private fun saveImage(finalBitmap: Bitmap, news: News) {
        val root = context.getExternalFilesDir(null)
        val myDir = File("$root/NEWS_IMAGES")
        myDir.mkdirs()
        val fname = "${news.id}.jpg"
        val file = File(myDir, fname)
        file.mkdirs()
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getImage(news: News): String? {
        val root = context.getExternalFilesDir(null)
        val myDir = File("$root/NEWS_IMAGES")
        val fname = "${news.id}.jpg"
        val savedFile = File(myDir, fname)
        return if (savedFile.exists()) {
            Uri.fromFile(savedFile).toString()
        } else {
            null
        }

    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    fun deleteDownloadedImage(news: News) {

        val root = context.getExternalFilesDir(null)
        val myDir = File("$root/NEWS_IMAGES")
        val fname = "${news.id}.jpg"
        val savedFile = File(myDir, fname)
         if (savedFile.exists()) {
            savedFile.delete()
        }
    }
}