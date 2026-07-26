package com.awais.jsonlauncher.models

data class NotificationInfo(
    val key:String,
    val packageName: String,
    val appName: String,
    val title: String?,
    val text: String?,
    val postTime: Long,
    val isCollapsed: Boolean = false
)