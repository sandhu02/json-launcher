package com.awais.jsonlauncher.ui.home.notification

import androidx.compose.foundation.clickable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.home.apps.AppsSectionViewModel
import com.awais.jsonlauncher.ui.jsonObject.JsonItem
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax
import com.awais.jsonlauncher.ui.theme.SyntaxColors

@Composable
fun NotificationsSection(
    viewModel : NotificationsSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val isCollapsed = state.isCollapsed

    Column(modifier = Modifier.padding(start = JsonSpacing.Indent)) {
        Row() {
            Text(if (isCollapsed) ">" else "˅" ,
                modifier = Modifier.clickable {
                    viewModel.onCollapseClick()
                }
            )

            Spacer(modifier = Modifier.width(JsonSpacing.XS))

            Text("\"notifications\"" , color = JsonSyntax.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("[" , color = JsonSyntax.parenthesis)

            if (isCollapsed){
                Text("..")
                Text("]", color = JsonSyntax.parenthesis)
                Text(",")
            }

        }

        if (!isCollapsed){
            LazyColumn() {
                items(
                    items = state.notifications,
                    key = { it.key }
                ) { notification ->
                    val notificationProperties = remember(notification.key) {
                        listOf(
                            JsonProperty(key = "title", value = notification.title ?: ""),
                            JsonProperty(key = "text", value = notification.text ?: "")
                        )
                    }
                    JsonItem(
                        name = notification.appName,
                        properties = notificationProperties,
                        isCollapsed = false,
                        onCollapseClick = {},
                        onClick = {}
                    )
                }
            }

            Row {
                Text("]" , color = JsonSyntax.parenthesis)
                Text(",")
            }

        }
    }
}