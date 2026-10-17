package com.awais.jsonlauncher.ui.screens.premiumScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun PaywallPlanCard(
    duration : String,
    price : String,
    billed : String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable { onClick() } ,
        shape = RoundedCornerShape(10.dp),
        border = if (isSelected) BorderStroke(1.6.dp, color = JsonSyntax.colors.boolean) else BorderStroke(0.8.dp, color = Color(0xff2A2A36)),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xff1A1626) else Color(0xff14141C)
        )
    ) {
        Column (
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text("\"${duration}\"", color = JsonSyntax.colors.key)
                Text(":")

                Spacer(modifier = Modifier.width(JsonSpacing.SM))

                Text("{", color = JsonSyntax.colors.parenthesis)
            }
            Column(
                modifier = Modifier.padding(start = JsonSpacing.Indent),
            ){
                Row(modifier = Modifier.padding(start = JsonSpacing.Indent)) {
                    Text("\"price\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("\"${price}\"", color = JsonSyntax.colors.string)
                    Text(",")
                }

                Row(modifier = Modifier.padding(start = JsonSpacing.Indent)) {
                    Text("\"billed\"", color = JsonSyntax.colors.key)
                    Text(":")

                    Spacer(modifier = Modifier.width(JsonSpacing.SM))

                    Text("\"${billed}\"", color = JsonSyntax.colors.string)
                }
            }
            Row {
                Text("}", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }
        }
    }
}