package com.awais.jsonlauncher.ui.home.system

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.jsonObject.JsonItem


@Composable
fun SystemSection(
    viewModel: SystemSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    val properties = listOf(
        JsonProperty("time","22:32"),
        JsonProperty("battery","14"),
        JsonProperty("wifi","true")
    )
    JsonItem(
        name = "system",
        properties = properties,
        onClick = {},
        onCollapseClicked = { viewModel.onCollapseClick() },
        isCollapsed = state.isCollapsed,
    )
}