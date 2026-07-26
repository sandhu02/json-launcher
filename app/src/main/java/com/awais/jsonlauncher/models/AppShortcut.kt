package com.awais.jsonlauncher.models

data class AppShortcut(
    val id: String,
    val shortLabel: String,
    val longLabel: String?,
    val rank: Int
)