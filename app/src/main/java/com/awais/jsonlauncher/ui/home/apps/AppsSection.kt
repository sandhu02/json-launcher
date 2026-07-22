package com.awais.jsonlauncher.ui.home.apps

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.home.HomeScreenViewModel
import com.awais.jsonlauncher.ui.jsonObject.JsonItem
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun AppsSection(
    viewModel: AppsSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.padding(start = JsonSpacing.Indent)) {
        Row() {
            Text ("")

            Spacer(modifier = Modifier.padding(JsonSpacing.XS))

            Text("\"apps\"" , color = JsonSyntax.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{" , color = JsonSyntax.parenthesis)
        }

        state.pinnedApps.forEach { app ->
            val appProperties = listOf(
                JsonProperty("packageName",app.packageName),
            )

            JsonItem(
                name = app.name,
                properties = appProperties,
                isCollapsed = state.isCollapsed,
                onClick = {
                    val intent = context.packageManager
                        .getLaunchIntentForPackage(app.packageName)

                    intent?.let {
                        context.startActivity(it)
                    }
                },
                onCollapseClicked = { viewModel.onCollapseClick() },
            )
        }

        Row() {
            Text("}", color = JsonSyntax.parenthesis)
            Text(",")
        }
    }

}