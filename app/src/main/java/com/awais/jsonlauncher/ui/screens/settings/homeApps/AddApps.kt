package com.awais.jsonlauncher.ui.screens.settings.homeApps

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.ui.components.SearchBar
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax


@Composable
fun AddApps(
    apps : List<AppInfo>,
    isAddCollapsed : Boolean,
    onAddCollapseClick : () -> Unit,
    addToHomeApps : (String) -> Unit,
    searchQuery : String,
    onSearchQueryChange : (String) -> Unit
) {
    val filteredApps = remember(
        apps,
        searchQuery
    ) {
        if (searchQuery.isBlank()) {
            apps
        } else {
            apps.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Row() {
        Text(if (isAddCollapsed) ">" else "⌄" ,
            modifier = Modifier.clickable {
                onAddCollapseClick()
            }
        )

        Spacer(modifier = Modifier.width(JsonSpacing.XS))

        Text("\"add\"", color = JsonSyntax.colors.boolean)
        Text(":")

        Spacer(modifier = Modifier.width(JsonSpacing.SM))

        Text("{", color = JsonSyntax.colors.parenthesis)

        if (isAddCollapsed) {
            Text("..")
            Text("}" , color = JsonSyntax.colors.parenthesis)
            Text(",")
        }

    }

    if (!isAddCollapsed) {
        Column(
            modifier = Modifier.padding(start = JsonSpacing.Indent)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = { onSearchQueryChange(it) },
                modifier = Modifier.padding(bottom = JsonSpacing.SM)
            )

            filteredApps.forEach { app ->
                Row() {
                    Text("+" ,
                        modifier = Modifier.clickable {
                            addToHomeApps(app.packageName)
                        }
                    )

                    Spacer(modifier = Modifier.width(JsonSpacing.XS))

                    Text("\"${app.name}\"" , color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("{" , color = JsonSyntax.colors.parenthesis)
                    Text("..")
                    Text("}" , color = JsonSyntax.colors.parenthesis)
                    Text(",")
                }
            }
        }
    }

    if (!isAddCollapsed) {
        Row() {
            Text("}" ,color = JsonSyntax.colors.parenthesis)
            Text(",")
        }
    }
}