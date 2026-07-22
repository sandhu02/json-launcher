package com.awais.jsonlauncher.repositories

import android.content.Context
import android.content.pm.LauncherApps
import android.os.Process
import com.awais.jsonlauncher.models.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var cachedApps: List<AppInfo>? = null

    fun getInstalledApps(): List<AppInfo> {

        cachedApps?.let { return it }

        val launcherApps = context.getSystemService(LauncherApps::class.java)

        cachedApps = launcherApps
            .getActivityList(null, Process.myUserHandle())
            .sortedBy { it.label.toString() }
            .map {
                AppInfo(
                    name = it.label.toString(),
                    packageName = it.applicationInfo.packageName,
//                    icon = it.getIcon(0)
                )
            }

        return cachedApps!!
    }
}