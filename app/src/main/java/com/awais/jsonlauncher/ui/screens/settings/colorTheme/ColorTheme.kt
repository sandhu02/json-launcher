package com.awais.jsonlauncher.ui.screens.settings.colorTheme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.awais.jsonlauncher.models.ThemeMode
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun ColorTheme(
    viewModel: ColorThemeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(start = JsonSpacing.Indent),
    ) {
        Row {
            Text(
                text = if (state.isCollapsed) ">" else "⌄",
                modifier = Modifier.clickable {
                    viewModel.onCollapseClick()
                }
            )

            Spacer(modifier = Modifier.width(JsonSpacing.XS))

            Text("\"color theme\"", color = JsonSyntax.colors.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{", color = JsonSyntax.colors.parenthesis)

            if (state.isCollapsed){
                Row{
                    Text("..")
                    Text("}" , color = JsonSyntax.colors.parenthesis)
                    Text(",")
                }
            }

        }

        if (!state.isCollapsed) {
            Column(
                modifier = Modifier.padding(start = JsonSpacing.Indent),
            ) {
                Text("/*Click on true or false to switch*/" , color = JsonSyntax.colors.comment)

                ThemeOption(
                    name = "light",
                    selected = state.themeMode == ThemeMode.LIGHT,
                    onClick = {
                        viewModel.onLightClicked()
                    }
                )

                ThemeOption(
                    name = "dark",
                    selected = state.themeMode == ThemeMode.DARK,
                    onClick = {
                        viewModel.onDarkClicked()
                    }
                )

                ThemeOption(
                    name = "system",
                    selected = state.themeMode == ThemeMode.SYSTEM,
                    onClick = {
                        viewModel.onSystemClicked()
                    }
                )

    //            Row {
    //                Text(
    //                    if (false) "◉" else "○",
    //                    modifier = Modifier.clickable {
    //
    //                    }
    //                )
    //                Spacer(modifier = Modifier.width(JsonSpacing.SM))
    //                Text("system" , color = JsonSyntax.colors.string)
    //            }

            }


            Row {
                Text("}", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }

        }
    }
}

@Composable
fun ThemeOption(
    name: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row {
        Text("\"$name\"", color = JsonSyntax.colors.key)
        Text(":")

        Spacer(modifier = Modifier.width(JsonSpacing.SM))

        Text(
            if (selected) "true" else "false",
            color = if (selected) JsonSyntax.colors.boolean else JsonSyntax.colors.string,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                onClick()
            }
        )
        Text(",")
    }
}