package com.awais.jsonlauncher

import android.app.role.RoleManager
import android.content.ComponentName
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.graphics.drawable.toDrawable
import com.awais.jsonlauncher.repositories.SettingsRepository
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val notificationPermissionRequestCode = 1001
    private val showNotificationDialog = mutableStateOf(false)

    private val showSetAsDefaultDialog = mutableStateOf(false)

    private val requestHomeRoleLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            // User returned from the system dialog.
            // Re-check whether we're now the default launcher.
            checkAndSetAsDefaultLauncher()
        }

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Check and request default launcher
        checkAndSetAsDefaultLauncher()

        // Check and request notification permission
        checkAndRequestNotificationPermission()

        window.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER
        )
        window.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        setContent {
            SetJsonLauncherTheme(
                settingsRepository = settingsRepository,
                showNotificationDialog = showNotificationDialog.value,
                onDismissNotificationDialog = {
                    showNotificationDialog.value = false
                },
                showSetAsDefaultDialog = showSetAsDefaultDialog.value,
                onDismissSetAsDefaultDialog = {
                    showSetAsDefaultDialog.value = false
                },
                onOpenSetAsDefault = {
                    requestHomeRole()
                }
            )
        }
    }

    private fun checkAndSetAsDefaultLauncher() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = getSystemService(RoleManager::class.java)

            // Already the default launcher
            if (roleManager.isRoleHeld(RoleManager.ROLE_HOME)) {
                showSetAsDefaultDialog.value = false
                return
            }

            // Not the default launcher
            showSetAsDefaultDialog.value = true
        }
    }

    fun requestHomeRole() {
        val roleManager = getSystemService(RoleManager::class.java)

        if (
            roleManager.isRoleAvailable(RoleManager.ROLE_HOME) &&
            !roleManager.isRoleHeld(RoleManager.ROLE_HOME)
        ) {
            requestHomeRoleLauncher.launch(
                roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
            )
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