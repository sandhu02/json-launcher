//package com.awais.jsonlauncher.ui.jsonObject
//
//import androidx.compose.foundation.layout.Row
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import com.awais.jsonlauncher.ui.theme.JsonSyntax
//
//@Composable
//fun JsonPropertyRenderer(property: JsonProperty) {
//
//    Row {
//
//        Text(
//            "\"${property.key}\"",
//            color = JsonSyntax.key
//        )
//
//        Text(": ")
//
//        JsonNodeRenderer(property.value)
//    }
//}