package com.awais.jsonlauncher.ui.home.apps

import android.content.Intent
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.jsonObject.JsonItem
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun AppsSection(
    viewModel: AppsSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(start = JsonSpacing.Indent)
    ) {
        Row() {
            Text (if (state.isCollapsed) ">" else "⌄" ,
                modifier = Modifier.clickable {
                    viewModel.onCollapseClick()
                }
            )

            Spacer(modifier = Modifier.padding(JsonSpacing.XS))

            Text("\"apps\"" , color = JsonSyntax.colors.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{" , color = JsonSyntax.colors.parenthesis)

            if (state.isLoading){
                Text("...")
            }

            if (state.isCollapsed) {
                Text("..")
                Text("}" , color = JsonSyntax.colors.parenthesis)
                Text(",")
            }
        }

        if (!state.isCollapsed){

            if (!state.isLoading) {
                state.pinnedApps.forEach { app ->
                    val appProperties = remember(app.packageName) {
                        buildList {
                            add(JsonProperty("packageName", app.packageName))
                            add(JsonProperty(
                                "Info",
                                "launch" ,
                                "BOOLEAN",
                                onValueClick = {
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                        data = Uri.fromParts("package", app.packageName, null)
                                    }
                                    context.startActivity(intent)
                                }
                            )
                            )
                            app.shortcuts.forEach { shortcut ->
                                add(
                                    JsonProperty(
                                        key = shortcut.shortLabel,
                                        value = "launch",
                                        valueType = "BOOLEAN",
                                        onValueClick = {
                                            viewModel.launchShortcut(
                                                app.packageName,
                                                shortcut.id
                                            )
                                        }
                                    )
                                )
                            }
                        }
                    }

                    JsonItem(
                        name = app.name,
                        properties = appProperties,
                        isCollapsed = app.isCollapsed,
                        onClick = {
                            val intent = context.packageManager
                                .getLaunchIntentForPackage(app.packageName)

                            intent?.let {
                                context.startActivity(it)
                            }
                        },
                        onCollapseClick = { viewModel.onAppCollapseClick(app.packageName) },
                    )
                }
            }

            Row() {
                Text("}", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }
        }
    }
}