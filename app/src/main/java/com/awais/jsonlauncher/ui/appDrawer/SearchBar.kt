package com.awais.jsonlauncher.ui.appDrawer

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(bottom = JsonSpacing.Gutter)
    ) {
        Text("sys" , color = JsonSyntax.key)
        Text(".")
        Text("launch" , color = JsonSyntax.boolean)
        Text("(" , color = JsonSyntax.parenthesis)
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.width(IntrinsicSize.Min),
            textStyle = LocalTextStyle.current.copy(
                color = JsonSyntax.string
            ),
            cursorBrush = SolidColor(JsonSyntax.string),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        "_",
                        color = JsonSyntax.comment
                    )
                }
                innerTextField()
            }
        )

        Text(")" , color = JsonSyntax.parenthesis)
    }
}