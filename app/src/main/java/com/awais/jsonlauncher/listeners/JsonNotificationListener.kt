package com.awais.jsonlauncher.listeners

import android.app.Notification
import android.content.pm.PackageManager
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.awais.jsonlauncher.models.NotificationInfo
import com.awais.jsonlauncher.repositories.NotificationRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class JsonNotificationListener : NotificationListenerService() {

    @Inject
    lateinit var repository: NotificationRepository

    private fun getAppName(packageName: String): String {
        return try {
            val pm = packageManager
            val ai = pm.getApplicationInfo(packageName, 0)
            pm.getApplicationLabel(ai).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            packageName
        }
    }

    private fun StatusBarNotification.toNotificationInfo(): NotificationInfo {
        val extras = notification.extras
        val title = extras.getString(Notification.EXTRA_TITLE) ?: extras.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString()
        
        return NotificationInfo(
            key = key,
            packageName = packageName,
            appName = getAppName(packageName),
            title = title ?: "No Title",
            text = text ?: "No Content",
            postTime = postTime
        )
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("JsonNotificationListener", "Service Connected")
        repository.setServiceConnected(true)

        // Load existing notifications
        try {
            val notifications = activeNotifications?.map { it.toNotificationInfo() } ?: emptyList()
            repository.setNotifications(notifications)
        } catch (e: Exception) {
            Log.e("JsonNotificationListener", "Error fetching active notifications", e)
        }
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("JsonNotificationListener", "Service Disconnected")
        repository.setServiceConnected(false)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("JsonNotificationListener", "Notification Posted: ${sbn.packageName}")
        // Filter out ongoing (like music players or system status) if you only want dismissible ones
        val isOngoing = (sbn.notification.flags and Notification.FLAG_ONGOING_EVENT) != 0
        if (!isOngoing) {
            repository.addNotification(sbn.toNotificationInfo())
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("JsonNotificationListener", "Notification Removed: ${sbn.packageName}")
        repository.removeNotification(sbn.packageName, sbn.postTime)
    }
}
