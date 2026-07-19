package com.awais.jsonlauncher.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.viewmodels.HomeScreenViewModel
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(),
) {
    val context = LocalContext.current

    val state by viewModel.uiState.collectAsState()

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(4)
    ) {

        items(state.apps) { app ->

            AppItem(
                app = app,
                onClick = {
                    val intent = context.packageManager
                        .getLaunchIntentForPackage(app.packageName)

                    intent?.let {
                        context.startActivity(it)
                    }
                }
            )

        }
    }
}

@Composable
fun AppItem(
    app: AppInfo,
    onClick: () -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onClick()
        }
    ) {

        Image(
            painter = rememberDrawablePainter(app.icon),
            contentDescription = app.name
        )

        Text(app.name)
    }
}