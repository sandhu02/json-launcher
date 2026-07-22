package com.awais.jsonlauncher.ui.jsonObject

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun JsonItem(
    name:String,
    properties: List<JsonProperty>,
    isCollapsed: Boolean,
    onCollapseClick : () -> Unit,
    onClick : () -> Unit,
) {

    Column(
        modifier = Modifier
            .padding(start = JsonSpacing.Indent)
            .clickable { onClick() }
    ) {
        Row() {
            Text(if (isCollapsed) ">" else "˅" ,
                modifier = Modifier.clickable {
                    onCollapseClick()
                }
            )

            Spacer(modifier = Modifier.width(JsonSpacing.XS))

            Text("\"${name}\"" , color = JsonSyntax.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{" , color = JsonSyntax.parenthesis)

            if (isCollapsed) {
                Text("..")
                Text("}", color = JsonSyntax.parenthesis)
                Text(",")
            }

        }

        if (!isCollapsed) {
            properties.forEach {
                Row(modifier = Modifier.padding(start = JsonSpacing.Indent)) {
                    Text("\"${it.key}\"", color = JsonSyntax.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    when(it.valueType) {
                        "STRING" -> Text(
                            "\"${it.value}\"", color = JsonSyntax.string,
                            modifier = Modifier.clickable{ it.onValueClick() }
                        )
                        "INTEGER" -> Text(
                            it.value , color = JsonSyntax.number,
                            modifier = Modifier.clickable{ it.onValueClick() }
                        )
                        "BOOLEAN" -> Text(
                            it.value , color = JsonSyntax.boolean , textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable{ it.onValueClick() }
                        )
                        "COMMENT" -> Text(
                            it.value , color = JsonSyntax.comment , textDecoration = TextDecoration.Underline,
                            modifier = Modifier.clickable{ it.onValueClick() }
                        )
                    }

                    Text(",")
                }
            }
            Row() {
                Text("}", color = JsonSyntax.parenthesis)
                Text(",")
            }

        }

    }
}