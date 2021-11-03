package com.ahmedmatem.android.matura.network.services

import com.ahmedmatem.android.matura.local.preferences.UserPrefs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class EmailConfirmService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(token: String) {
        UserPrefs(applicationContext).setFcmToken(token)
    }
}