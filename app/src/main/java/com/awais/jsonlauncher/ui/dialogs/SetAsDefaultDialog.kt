package com.awais.jsonlauncher.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun SetAsDefaultDialog(
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
//            shape = RoundedCornerShape(20.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    text = "\"Set as Default Launcher\"",
                    style = MaterialTheme.typography.titleLarge,
                    color = JsonSyntax.colors.key
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "/* Set Json Launcher as your default Home Screen Launcher */",
                    color = JsonSyntax.colors.comment
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss
                    ) {
                        Text("Cancel", style = MaterialTheme.typography.labelLarge)
                    }

                    Spacer(Modifier.width(8.dp))

                    OutlinedButton(
                        border = BorderStroke(1.dp, color = JsonSyntax.colors.parenthesis),
                        onClick = onOpenSettings
                    ) {
                        Text("Open Settings")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SetDefaultPreview() {
    SetAsDefaultDialog(
        {},{}
    )
}