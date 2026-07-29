package com.awais.jsonlauncher.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class SyntaxColors(
    val key: Color,
    val string: Color,
    val number: Color,
    val boolean: Color,
    val comment: Color,
    val parenthesis: Color,
    val itemParenthesis: Color
)
val LocalSyntaxColors = staticCompositionLocalOf<SyntaxColors> {
    error("No SyntaxColors provided")
}

val JsonSyntaxDark = SyntaxColors(
    key = JsonKeyDark,
    string = JsonStringDark,
    number = JsonNumberDark,
    boolean = JsonBooleanDark,
    comment = JsonCommentDark,
    parenthesis = JsonParenthesisDark,
    itemParenthesis = JsonItemParenthesisDark
)

val jsonSyntaxLight = SyntaxColors(
    key = JsonKeyLight,
    string = JsonStringLight,
    number = JsonNumberLight,
    boolean = JsonBooleanLight,
    comment = JsonCommentLight,
    parenthesis = JsonParenthesisLight,
    itemParenthesis = JsonItemParenthesisLight
)
