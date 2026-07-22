package com.awais.jsonlauncher.ui.theme

import androidx.compose.ui.graphics.Color

data class SyntaxColors(
    val key: Color,
    val string: Color,
    val number: Color,
    val boolean: Color,
    val comment: Color,
    val parenthesis: Color
)

val JsonSyntax = SyntaxColors(
    key = JsonKey,
    string = JsonString,
    number = JsonNumber,
    boolean = JsonBoolean,
    comment = JsonComment,
    parenthesis = JsonParenthesis
)