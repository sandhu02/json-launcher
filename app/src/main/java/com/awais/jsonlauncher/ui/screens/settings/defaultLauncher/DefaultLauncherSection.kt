package com.awais.jsonlauncher.ui.screens.settings.defaultLauncher

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun DefaultLauncherSection(
    viewModel: DefaultLauncherSectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    val context = LocalContext.current

    val roleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { /* Handle result if needed */ }

    fun openDefaultLauncherPrompt(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val roleManager = context.getSystemService(Context.ROLE_SERVICE) as RoleManager
            if (roleManager.isRoleAvailable(RoleManager.ROLE_HOME) &&
                !roleManager.isRoleHeld(RoleManager.ROLE_HOME)) {

                val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_HOME)
                roleLauncher.launch(intent)
            } else {
                // App is already default, or role isn't available: open Home Settings directly
                val intent = Intent(Settings.ACTION_HOME_SETTINGS)
                context.startActivity(intent)
            }
        } else {
            // Fallback for Android 9 (API 28) and lower
            val intent = Intent(Settings.ACTION_HOME_SETTINGS)
            context.startActivity(intent)
        }
    }

    Column(
        modifier = Modifier.padding(start = JsonSpacing.Indent),
    ) {
        Row {
            Text (
                text = if (state.isCollapsed) ">" else "⌄" ,
                modifier = Modifier.clickable {
                    viewModel.onCollapseClick()
                }
            )

            Spacer(modifier = Modifier.width(JsonSpacing.XS))

            Text("\"default Launcher\"", color = JsonSyntax.colors.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{", color = JsonSyntax.colors.parenthesis)

            if (state.isCollapsed){
                Row{
                    Text("..")
                    Text("}" , color = JsonSyntax.colors.parenthesis)
                    Text(",")
                }
            }
        }

        if (!state.isCollapsed) {
            Column(
                modifier = Modifier.padding(start = JsonSpacing.Indent),
            ){
                Row() {
                    Text("/*Change your default Launcher*/" , color = JsonSyntax.colors.comment)
                }
                Row() {
                    Text("\"switch\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text(
                        "launch", color = JsonSyntax.colors.boolean ,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable{
                            openDefaultLauncherPrompt(context)
                        }
                    )
                }
            }
            Row {
                Text("}", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }
        }
    }
}