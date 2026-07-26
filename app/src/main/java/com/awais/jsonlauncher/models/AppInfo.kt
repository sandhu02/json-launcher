package com.awais.jsonlauncher.models

data class AppInfo(
    val name: String,
    val packageName: String,
//    val icon: Drawable,
    val isCollapsed: Boolean = true,
    val shortcuts: List<AppShortcut>
)