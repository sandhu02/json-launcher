package com.awais.jsonlauncher.ui.appDrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.jsonObject.JsonItem
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun AppDrawerScreen(
    viewModel : AppDrawerScreenViewModel = hiltViewModel (),
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = modifier) {
        SearchBar(
            query = "",
            onQueryChange = {}
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            item {
                Row {
                    Text("")

                    Spacer(modifier = Modifier.width(JsonSpacing.XS))

                    Text("\"apps\"", color = JsonSyntax.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("{", color = JsonSyntax.parenthesis)
                }
            }

            items(
                items = state.apps,
                key = { it.packageName }
            ) { app ->

                val appProperties = remember(app.packageName) {
                    listOf(
                        JsonProperty("packageName", app.packageName)
                    )
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
                    Text("}", color = JsonSyntax.parenthesis)
                    Text(",")
                }
            }
        }
    }
}