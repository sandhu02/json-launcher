package com.awais.jsonlauncher.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.awais.jsonlauncher.ui.settings.defaultLauncher.DefaultLauncherSection
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

                    Text("\"settings\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("{", color = JsonSyntax.colors.parenthesis)
                }

            }
            item {
                DefaultLauncherSection()
            }
            item {
                HomeApps()
            }

            item {
                Row {
                    Text("}", color = JsonSyntax.colors.parenthesis)
                    Text(",")
                }
            }
        }

    }
}