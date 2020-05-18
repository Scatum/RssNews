package com.news.worker

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.news.MainActivity
import com.news.repository.NetworkDataRepository
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.lang.Exception


class AppWorker(
     private val mContext: Context,
     private val workerParams: WorkerParameters
) :
    Worker(mContext, workerParams), KoinComponent {
    private val networkDataRepository: NetworkDataRepository by inject()
    override fun doWork(): Result {
        try {
            Log.e("doWork", "start")
            networkDataRepository.fetchData()
            return Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("doWork", "IOException ${e.message}")

        }
        return Result.retry()
    }

}