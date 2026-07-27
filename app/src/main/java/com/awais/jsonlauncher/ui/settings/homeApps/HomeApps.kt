package com.awais.jsonlauncher.ui.settings.homeApps

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun HomeApps(
    viewModel: HomeAppsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(start = JsonSpacing.Indent),
    ) {
        Row {
            Text (if (state.isCollapsed) ">" else "⌄" ,
                modifier = Modifier.clickable {
                    viewModel.onCollapseClick()
                }
            )

            Spacer(modifier = Modifier.width(JsonSpacing.XS))

            Text("\"home apps\"", color = JsonSyntax.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{", color = JsonSyntax.parenthesis)

            if (state.isCollapsed) {
                Text("..")
                Text("}" , color = JsonSyntax.parenthesis)
                Text(",")
            }
        }

        Column(
            modifier = Modifier.padding(start = JsonSpacing.Indent)
        ) {
            if (!state.isCollapsed){
                if (!state.isLoading) {
                    state.pinnedApps.forEach { app ->
                        Row() {
                            Text("-" ,
                                modifier = Modifier.clickable {
                                    viewModel.removeFromHomeApps(app.packageName)
                                }
                            )

                            Spacer(modifier = Modifier.width(JsonSpacing.XS))

                            Text("\"${app.name}\"" , color = JsonSyntax.key)
                            Text(":")

                            Spacer(modifier = Modifier.width(JsonSpacing.SM))

                            Text("{" , color = JsonSyntax.parenthesis)
                            Text("..")
                            Text("}", color = JsonSyntax.parenthesis)
                            Text(",")
                        }
                    }


                    AddApps(
                        apps = state.apps,
                        isAddCollapsed = state.isAddCollapsed,
                        onAddCollapseClick = { viewModel.onAddCollapseClick() },
                        addToHomeApps = {
                            packageName ->  viewModel.addToHomeApps(packageName)
                        },
                    )

                }

            }
        }

        if (!state.isCollapsed){
            Row() {
                Text("}", color = JsonSyntax.parenthesis)
                Text(",")
            }
        }

    }
}