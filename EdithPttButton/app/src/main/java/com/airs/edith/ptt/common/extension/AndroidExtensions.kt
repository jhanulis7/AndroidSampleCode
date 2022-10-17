package com.airs.edith.ptt.common.extension

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.airs.edith.ptt.R

fun Context.createNotification(
    @DrawableRes iconId: Int = R.mipmap.ic_launcher_round,
    channelId : String = "",
    textTitle: String = "",
    textContent: String = "",
    ticker: String = "",
    contentIntent: () -> PendingIntent,
): Notification = NotificationCompat.Builder(this, channelId)
    .setSmallIcon(iconId)
    .setContentTitle(textTitle)
    .setContentText(textContent)
    .setContentIntent(contentIntent())
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setTicker(ticker)
    .build()

@RequiresApi(Build.VERSION_CODES.O)
fun Context.createNotificationChannel(
    channel: NotificationChannel
){
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}