package com.awais.jsonlauncher.repositories

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Build
import android.os.Process
import android.util.Log
import androidx.annotation.RequiresApi
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.models.AppShortcut
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

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


    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun getAppShortcuts(
        launcherApps: LauncherApps
    ): Map<String, List<AppShortcut>> {

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
}