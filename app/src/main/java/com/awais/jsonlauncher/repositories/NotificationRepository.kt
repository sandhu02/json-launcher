package com.awais.jsonlauncher.repositories

import android.util.Log
import com.awais.jsonlauncher.models.NotificationInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor() {

    private val _notifications = MutableStateFlow<List<NotificationInfo>>(emptyList())
    val notifications: StateFlow<List<NotificationInfo>> = _notifications.asStateFlow()

    private val _isServiceConnected = MutableStateFlow(false)
    val isServiceConnected: StateFlow<Boolean> = _isServiceConnected.asStateFlow()

    fun setServiceConnected(connected: Boolean) {
        Log.d("NotificationRepository", "Service connection status: $connected")
        _isServiceConnected.value = connected
    }

    fun setNotifications(notifications: List<NotificationInfo>) {
        Log.d("NotificationRepository", "Setting ${notifications.size} notifications")
        _notifications.value = notifications
    }

    fun addNotification(notification: NotificationInfo) {
        Log.d("NotificationRepository", "Adding notification from: ${notification.appName}")
        _notifications.update { current ->
            // Add new at the top, remove duplicates
            listOf(notification) + current.filter {
                !(it.packageName == notification.packageName && it.postTime == notification.postTime)
            }
        }
    }

    fun removeNotification(packageName: String, postTime: Long) {
        Log.d("NotificationRepository", "Removing notification from: $packageName")
        _notifications.update {
            it.filterNot { n -> n.packageName == packageName && n.postTime == postTime }
        }
    }
}
