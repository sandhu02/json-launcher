package com.awais.jsonlauncher

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.awais.jsonlauncher.ui.JsonLauncher
import com.awais.jsonlauncher.ui.theme.JsonLauncherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val notificationPermissionRequestCode = 1001
    private val showNotificationDialog = mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check and request notification permission
        checkAndRequestNotificationPermission()

        setContent {
            JsonLauncherTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JsonLauncher(
                        modifier = Modifier.padding(innerPadding),
                        showNotificationDialog = showNotificationDialog.value,
                        onDismissNotificationDialog = {
                            showNotificationDialog.value = false
                        }
                    )
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    notificationPermissionRequestCode
                )
            }
        }


        // Check notification listener access
        if (!isNotificationServiceEnabled()) {
            Log.d("MainActivity", "Notification Service NOT enabled. Opening settings.")
            showNotificationDialog.value = true
        } else {
            Log.d("MainActivity", "Notification Service is enabled.")
        }
    }

    override fun onResume() {
        super.onResume()
        // Check if notification access was granted while app was in background
        if (isNotificationServiceEnabled()) {
            Log.d("MainActivity", "Notification Service is now enabled")
            // Optionally trigger a refresh
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":")
            for (name in names) {
                val cn = ComponentName.unflattenFromString(name)
                if (cn != null && TextUtils.equals(pkgName, cn.packageName)) {
                    return true
                }
            }
        }
        return false
    }
}