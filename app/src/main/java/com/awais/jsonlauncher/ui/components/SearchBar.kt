package com.awais.jsonlauncher.ui.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Text("sys" , color = JsonSyntax.colors.key)
        Text(".")
        Text("find" , color = JsonSyntax.colors.boolean)
        Text("(" , color = JsonSyntax.colors.parenthesis)
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.width(IntrinsicSize.Min),
            textStyle = LocalTextStyle.current.copy(
                color = JsonSyntax.colors.string
            ),
            cursorBrush = SolidColor(JsonSyntax.colors.string),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        "_",
                        color = JsonSyntax.colors.comment
                    )
                }
                innerTextField()
            }
        )

        Text(")" , color = JsonSyntax.colors.parenthesis)
    }
}