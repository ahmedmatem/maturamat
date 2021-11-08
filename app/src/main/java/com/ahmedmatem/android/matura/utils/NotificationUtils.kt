package com.ahmedmatem.android.matura.utils

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
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

    val builder = NotificationCompat.Builder(
        applicationContext,
        channelId
    )

        .setSmallIcon(R.drawable.ic_check_outline)
        .setContentTitle(contentTitle)
        .setContentText(contentText)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    notify(NOTIFICATION_ID, builder.build())
}
