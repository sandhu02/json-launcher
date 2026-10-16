package com.awais.jsonlauncher.ui.screens.settings.getPremium

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.awais.jsonlauncher.ui.navigation.JsonLauncherAppScreens
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun GetPremiumScreen(
    viewModel: GetPremiumScreenViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state by viewModel.uiState.collectAsState()

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

            Text("\"Json Launcher Premium\"", color = JsonSyntax.colors.key)
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
                    Text("/*Get Premium to access more features*/" , color = JsonSyntax.colors.comment)
                }
                Row() {
                    Text("\"get_premium\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text(
                        "launch", color = JsonSyntax.colors.boolean ,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable{
                            navController.navigate(route = JsonLauncherAppScreens.PremiumScreen.name)
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