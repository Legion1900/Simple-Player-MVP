package com.legion1900.mvpplayer.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

@TargetApi(Build.VERSION_CODES.O)
class ServiceNotificationHelper(
    private val serviceId: Int,
    private val context: Context,
    channelId: String,
    channelName: String,
    importance: Int,
    iconResId: Int,
    intent: PendingIntent
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

    fun updateNotification(songName: String, text: String) {
        builder.setContentTitle(songName)
            .setContentText(text)
        val newNotification = builder.build()
        val manager =
            context.getSystemService<NotificationManager>(NotificationManager::class.java)
        manager?.notify(serviceId, newNotification)
    }
}