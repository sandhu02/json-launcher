package com.awais.jsonlauncher.ui.settings

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.home.apps.AppsSectionViewModel
import com.awais.jsonlauncher.ui.jsonObject.JsonItem
import com.awais.jsonlauncher.ui.settings.homeApps.HomeApps
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax


@Composable
fun LauncherSettings(
    modifier: Modifier = Modifier,
    viewModel: LauncherSettingsViewModel = hiltViewModel(),
    navController: NavHostController
) {
//    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(4.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Row {
                    Text("")

                    Spacer(modifier = Modifier.width(JsonSpacing.XS))

                    Text("\"settings\"", color = JsonSyntax.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("{", color = JsonSyntax.parenthesis)
                }

            }
            item {
                HomeApps()
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