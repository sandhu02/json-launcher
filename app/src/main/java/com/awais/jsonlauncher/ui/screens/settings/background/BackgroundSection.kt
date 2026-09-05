package com.awais.jsonlauncher.ui.screens.settings.background

import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.awais.jsonlauncher.models.BackgroundMode
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax


@Composable
fun BackgroundSection(
    viewModel: BackgroundSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

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

            Text("\"background\"", color = JsonSyntax.colors.key)
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
                Text("/*Click to select the background */" , color = JsonSyntax.colors.comment)

                BackgroundOption(
                    name = "default",
                    selected = state.backgroundMode == BackgroundMode.DEFAULT,
                    onClick = {
                        viewModel.onDefaultClicked()
                    }
                )

                BackgroundOption(
                    name = "wallpaper",
                    selected = state.backgroundMode == BackgroundMode.WALLPAPER,
                    onClick = {
                        viewModel.onWallpaperClicked()
                    }
                )

                Row {
                    Text("\"pick_wallpaper\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text(
                        text = "launch",
                        color = JsonSyntax.colors.boolean,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            val intent = Intent("android.intent.action.SET_WALLPAPER")

                            try {
                                context.startActivity(intent)
                            } catch (e: ActivityNotFoundException) {
                                Log.e("BackgroundSection", "Error starting activity: $e")
                                // No wallpaper picker available
                            }
                        }
                    )
                    Text(",")
                }

            }

            Row {
                Text("}", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }

        }
    }
}


@Composable
fun BackgroundOption(
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