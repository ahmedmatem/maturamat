package com.ahmedmatem.android.matura.network.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.ahmedmatem.android.matura.R
import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.ahmedmatem.android.matura.repository.AccountRepository
import com.ahmedmatem.android.matura.utils.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class EmailConfirmService : FirebaseMessagingService() {
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _accountRepository: AccountRepository by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Set EmailConfirmed in database
        remoteMessage.data["email"]?.let { email ->
            coroutineScope.launch {
                _accountRepository.setEmailConfirmed(email, true)
            }
        }

        // create notification channel
        val channelId = getString(R.string.confirm_email_notification_channel_id)
        createChannel(
            channelId,
            getString(R.string.confirm_email_notification_channel_name)
        )

        // send notification
        val notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(
            applicationContext,
            channelId,
            getString(R.string.confirm_email_notification_content_title),
            getString(R.string.confirm_email_notification__content_text)
        )
    }

    override fun onNewToken(token: String) {
        UserPrefs(applicationContext).setFcmToken(token)
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description =
                getString(R.string.confirm_email_notification_channel_description)

            val notificationManager =
                getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

        }
    }
}