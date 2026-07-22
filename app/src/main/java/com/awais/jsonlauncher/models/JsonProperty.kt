package com.awais.jsonlauncher.models

data class JsonProperty (
    val key: String,
    val value: String,
    val valueType: String = "STRING",
    val onValueClick: () -> Unit = {}
)