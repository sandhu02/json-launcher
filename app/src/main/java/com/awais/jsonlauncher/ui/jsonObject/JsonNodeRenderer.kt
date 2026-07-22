//package com.awais.jsonlauncher.ui.jsonObject
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.text.style.TextDecoration
//import com.awais.jsonlauncher.ui.theme.JsonSyntax
//
//
//@Composable
//fun JsonNodeRenderer(node: JsonNode) {
//    when (node) {
//
//        is JsonObject -> {
//            Column {
//                Text("{" , color = JsonSyntax.parenthesis)
//
//                node.properties.forEach {
//                    JsonPropertyRenderer(
//                        it
//                    )
//                }
//
//                Row(){
//                    Text("}" , color = JsonSyntax.parenthesis)
//                    Text(",")
//                }
//            }
//        }
//
//        is JsonArray -> {
//            Column {
//                Text("[" , color = JsonSyntax.parenthesis)
//                node.items.forEach {
//                    JsonNodeRenderer(it)
//                }
//                Text("]" , color = JsonSyntax.parenthesis)
//            }
//        }
//
//        is JsonString ->
//            Text(
//                "\"${node.value}\"",
//                color = JsonSyntax.string
//            )
//
//        is JsonNumber ->
//            Text(
//                node.value.toString(),
//                color = JsonSyntax.number
//            )
//
//        is JsonBoolean ->
//            Text(
//                node.value.toString(),
//                color = JsonSyntax.boolean,
//                textDecoration = TextDecoration.Underline
//            )
//
//        JsonNull ->
//            Text("null")
//    }
//}
