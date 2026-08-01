package com.awais.jsonlauncher.repositories

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.LauncherApps
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.models.AppShortcut
import com.awais.jsonlauncher.receivers.PackageChangeReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps = _apps.asStateFlow()

    private val receiver = PackageChangeReceiver {
        refreshApps()
    }

    private var isRegistered = false

    private var cachedApps: List<AppInfo>? = null

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getInstalledApps(): List<AppInfo> {

        cachedApps?.let { return it }

        val launcherApps = context.getSystemService(LauncherApps::class.java)

        val shortcutsByPackage = getAppShortcuts(launcherApps)

        cachedApps = launcherApps
            .getActivityList(null, Process.myUserHandle())
            .sortedBy { it.label.toString() }
            .map {
                AppInfo(
                    name = it.label.toString(),
                    packageName = it.applicationInfo.packageName,
                    shortcuts = shortcutsByPackage[
                        it.applicationInfo.packageName
                    ].orEmpty(),
//                    icon = it.getIcon(0)
                )
            }

        return cachedApps!!
    }

    fun refreshApps() {
        cachedApps = null
        _apps.value = getInstalledApps()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun getAppShortcuts(
        launcherApps: LauncherApps
    ): Map<String, List<AppShortcut>> {

        if (!launcherApps.hasShortcutHostPermission()) {
            return emptyMap()
        }


        val query = LauncherApps.ShortcutQuery().apply {
            setQueryFlags(
                LauncherApps.ShortcutQuery.FLAG_MATCH_DYNAMIC or
                        LauncherApps.ShortcutQuery.FLAG_MATCH_MANIFEST or
                        LauncherApps.ShortcutQuery.FLAG_MATCH_PINNED
            )
        }

        val shortcuts = launcherApps.getShortcuts(
            query,
            Process.myUserHandle()
        ) ?: emptyList()

        return shortcuts
            .groupBy { it.`package` }
            .mapValues { (_, shortcuts) ->
                shortcuts
                    .sortedBy { it.rank }
                    .map {
                        AppShortcut(
                            id = it.id,
                            shortLabel = it.shortLabel?.toString().orEmpty(),
                            longLabel = it.longLabel?.toString(),
                            rank = it.rank
                        )
                    }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun launchShortcut(
        packageName: String,
        shortcutId: String
    ) {
        val launcherApps = context.getSystemService(LauncherApps::class.java)

        if (!launcherApps.hasShortcutHostPermission()) {
            return
        }

        launcherApps.startShortcut(
            packageName,
            shortcutId,
            null,                   // sourceBounds
            null,                   // options Bundle
            Process.myUserHandle()
        )
    }


    fun registerReceiver() {
        if (isRegistered) return

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addAction(Intent.ACTION_PACKAGE_REPLACED)
            addDataScheme("package")
        }

        context.registerReceiver(receiver, filter)
        isRegistered = true
    }

    fun unregisterReceiver() {
        if (!isRegistered) return

        context.unregisterReceiver(receiver)
        isRegistered = false
    }

}