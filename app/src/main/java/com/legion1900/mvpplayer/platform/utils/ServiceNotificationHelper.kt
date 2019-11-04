package com.legion1900.mvpplayer.platform.utils

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

@TargetApi(Build.VERSION_CODES.O)
class ServiceNotificationHelper(
    private val context: Context,
    private val serviceId: Int,
    channelId: String,
    channelName: String,
    iconResId: Int,
    intent: PendingIntent,
    importance: Int = NotificationManager.IMPORTANCE_DEFAULT
) {
    /*
    * Creates notification channel.
    * */
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                importance
            )
            val manager =
                context.getSystemService<NotificationManager>(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(iconResId)
        .setContentIntent(intent)

    fun updateNotification(title: String, text: String) {
        val notification = buildNotification(title, text)
        val manager =
            context.getSystemService<NotificationManager>(NotificationManager::class.java)
        manager?.notify(serviceId, notification)
    }

    fun buildNotification(title: String, text: String): Notification {
        builder.setContentTitle(title)
            .setContentText(text)
        return builder.build()
    }
}