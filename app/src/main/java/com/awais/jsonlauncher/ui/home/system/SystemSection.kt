package com.awais.jsonlauncher.ui.home.system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.jsonObject.JsonItem


@Composable
fun SystemSection(
    viewModel: SystemSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    val properties = listOf(
        JsonProperty("time",state.time),
        JsonProperty("date",state.date),
        JsonProperty("battery",state.battery.toString() , valueType = "INTEGER"),
        JsonProperty("LauncherSettings","launch" , valueType = "COMMENT",
            onValueClick = {}
        )
    )
    JsonItem(
        name = "system",
        properties = properties,
        onClick = {},
        onCollapseClick = { viewModel.onCollapseClick() },
        isCollapsed = state.isCollapsed,
    )
}