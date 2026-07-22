//package com.awais.jsonlauncher.ui.jsonObject
//
//sealed interface JsonNode
//
//data class JsonObject(
//    val properties: List<JsonProperty>
//) : JsonNode
//
//data class JsonArray(
//    val items: List<JsonNode>
//) : JsonNode
//
//data class JsonProperty(
//    val key: String,
//    val value: JsonNode
//)
//
//data class JsonString(val value: String) : JsonNode
//data class JsonNumber(val value: Number) : JsonNode
//data class JsonBoolean(val value: Boolean) : JsonNode
//data object JsonNull : JsonNode