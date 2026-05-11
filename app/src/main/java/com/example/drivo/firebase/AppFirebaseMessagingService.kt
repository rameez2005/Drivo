package com.example.drivo.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import com.example.drivo.R
import com.example.drivo.activities.MainActivity
import com.example.drivo.data.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Simple FirebaseMessagingService to receive push notifications.
 * Register in manifest and ensure FCM is enabled in Firebase console.
 */
class AppFirebaseMessagingService : FirebaseMessagingService() {

    private val firestoreRepository by lazy { FirestoreRepository() }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        getSharedPreferences(PREFS, MODE_PRIVATE)
            .edit { putString(KEY_PENDING_TOKEN, token) }

        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            persistTokenForUser(userId, token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.notification?.title ?: "Drivo"
        val body = message.notification?.body ?: message.data["body"] ?: "You have a new message"

        sendNotification(title, body)
    }

    private fun sendNotification(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = "drivo_notifications"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NotificationManager::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Drivo Notifications", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Important Drivo alerts"
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notificationBuilder.build())
    }

    private fun persistTokenForUser(userId: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                firestoreRepository.updateUserToken(userId, token)
                getSharedPreferences(PREFS, MODE_PRIVATE)
                    .edit { remove(KEY_PENDING_TOKEN) }
            } catch (_: Exception) {
                // Keep the token locally; MainActivity can retry later.
            }
        }
    }

    companion object {
        private const val PREFS = "fcm_prefs"
        private const val KEY_PENDING_TOKEN = "pending_fcm_token"
    }
}

