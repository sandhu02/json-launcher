package com.awais.jsonlauncher.ui.home.system

import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.jsonObject.JsonItem


@Composable
fun SystemSection(
    viewModel: SystemSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val properties = listOf(
        JsonProperty("time",state.time),
        JsonProperty("date",state.date),
        JsonProperty("battery",state.battery.toString() , valueType = "INTEGER"),
        JsonProperty("isCharging",state.isCharging.toString() , valueType = "BOOLEAN"),
        JsonProperty("connection",state.networkState.toString() , valueType = "BOOLEAN" ,
            onValueClick = {
                val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                context.startActivity(intent)
            }
        ),
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