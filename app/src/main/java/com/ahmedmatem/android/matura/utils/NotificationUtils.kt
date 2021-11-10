package com.ahmedmatem.android.matura.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.ahmedmatem.android.matura.AccountActivity
import com.ahmedmatem.android.matura.R

private const val NOTIFICATION_ID = 0

/**
 * Extension function for NotificationManager
 */
fun NotificationManager.sendNotification(
    applicationContext: Context,
    channelId: String,
    contentTitle: String,
    contentText: String
) {
    // Intent to open an activity that correspondents to the notification
    val contentIntent = Intent(applicationContext, AccountActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val contentPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    )
        .setSmallIcon(R.drawable.ic_check_outline)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}
