package com.awais.jsonlauncher

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.awais.jsonlauncher.listeners.JsonNotificationListener
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class JsonLauncherApp : Application() {
    companion object {
        private const val TAG = "JsonLauncherApp"
    }

    override fun onCreate() {
        super.onCreate()

        // Register for app lifecycle callbacks to handle service state
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                // Check if notification listener is enabled and service is running
                if (activity is MainActivity) {
                    checkNotificationServiceState()
                }
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun checkNotificationServiceState() {
        val enabled = isNotificationServiceEnabled()
        Log.d(TAG, "Notification service state: $enabled")

        if (enabled) {
            // Ensure the service is running
            val intent = Intent(this, JsonNotificationListener::class.java)
            startService(intent)
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat?.contains(packageName) == true
    }
}