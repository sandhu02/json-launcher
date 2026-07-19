package com.awais.jsonlauncher.repositories

import com.awais.jsonlauncher.models.AppInfo
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class AppsRepository(
    private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun getInstalledApps(): List<AppInfo> {

        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val pm = context.packageManager

        return pm.queryIntentActivities(intent, 0)
            .sortedBy {
                it.loadLabel(pm).toString()
            }
            .map {
                AppInfo(
                    name = it.loadLabel(pm).toString(),
                    packageName = it.activityInfo.packageName,
                    icon = it.loadIcon(pm)
                )
            }
    }
}