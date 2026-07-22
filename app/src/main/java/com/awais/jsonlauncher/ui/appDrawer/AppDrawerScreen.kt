package com.awais.jsonlauncher.ui.appDrawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.home.apps.AppsSectionViewModel
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

        Column(
            modifier = Modifier
            .verticalScroll(rememberScrollState())
        ) {
            Row() {
                Text ("")

                Spacer(modifier = Modifier.padding(JsonSpacing.XS))

                Text("\"apps\"" , color = JsonSyntax.key)
                Text(":")

                Spacer(modifier = Modifier.width(JsonSpacing.SM))

                Text("{" , color = JsonSyntax.parenthesis)
            }

            state.apps.forEach { app ->
                val appProperties = listOf(
                    JsonProperty("packageName",app.packageName),
                )

                JsonItem(
                    name = app.name,
                    properties = appProperties,
                    isCollapsed = false,
                    onClick = {
                        val intent = context.packageManager
                            .getLaunchIntentForPackage(app.packageName)

                        intent?.let {
                            context.startActivity(it)
                        }
                    },
                    onCollapseClicked = { },
                )
            }

            Row() {
                Text("}", color = JsonSyntax.parenthesis)
                Text(",")
            }
        }
    }
}