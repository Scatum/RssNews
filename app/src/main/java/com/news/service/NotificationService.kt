package com.news.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.news.MainActivity
import com.news.R
import com.news.model.entity.News
import java.util.*

class NotificationService(private val context: Context) {
    private var notificationManager: NotificationManager? = null

    fun createNotification(news: News, downloaded : Boolean = false) {
        //You should use an actual ID instead
        val notificationId = Random().nextInt(60000)
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val r = RingtoneManager.getRingtone(context, notification)
        r.play()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels()
        }

        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("notification_id", news.id)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0 /* Request code */, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val notificationBuilder = NotificationCompat.Builder(context, ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.big_icon))
            .setContentTitle(if (!downloaded)news.title else "${news.title} Downloaded")
            .setContentText(news.pubDate)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setSound(notification)
        notificationManager!!.notify(notificationId, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels() {
        val adminChannelName: CharSequence = context.getString(R.string.app_name)
        val adminChannelDescription = context.getString(R.string.app_name)
        val adminChannel: NotificationChannel
        adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID,
            adminChannelName,
            NotificationManager.IMPORTANCE_LOW
        )
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        if (notificationManager != null) {
            notificationManager?.createNotificationChannel(adminChannel)
        }
    }

    companion object {
        private const val ADMIN_CHANNEL_ID = "admin_channel"
    }
}