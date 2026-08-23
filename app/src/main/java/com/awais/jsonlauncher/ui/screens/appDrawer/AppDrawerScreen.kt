package com.awais.jsonlauncher.ui.screens.appDrawer

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.components.SearchBar
import com.awais.jsonlauncher.ui.components.jsonObject.JsonItem
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax


@Composable
fun AppDrawerScreen(
    modifier: Modifier = Modifier,
    viewModel : AppDrawerScreenViewModel = hiltViewModel (),
    navController: NavHostController
) {
    BackHandler(enabled = true) {
        viewModel.resetQueryField()
    }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val filteredApps = remember(
        state.apps,
        state.searchQuery
    ) {
        if (state.searchQuery.isBlank()) {
            state.apps
        } else {
            state.apps.filter {
                it.name.contains(state.searchQuery, ignoreCase = true)
            }
        }
    }

    Column(modifier = modifier) {
        SearchBar(
            query = state.searchQuery,
            onQueryChange = { viewModel.onSearchQueryChange(it) },
            modifier = Modifier.padding(bottom = JsonSpacing.Gutter)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Row {
                    Text("")

                    Spacer(modifier = Modifier.width(JsonSpacing.XS))

                    Text("\"apps\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("{", color = JsonSyntax.colors.parenthesis)
                }
            }

            items(
                items = filteredApps,
                key = { it.packageName }
            ) { app ->

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

                        intent?.let(context::startActivity)
                    },
                    onCollapseClick = { viewModel.onCollapseClick(app.packageName) },
                )
            }

            item {
                Row {
                    Text("}", color = JsonSyntax.colors.parenthesis)
                    Text(",")
                }
            }
        }
    }
}