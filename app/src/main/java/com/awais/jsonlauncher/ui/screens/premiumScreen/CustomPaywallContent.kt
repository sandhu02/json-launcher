package com.awais.jsonlauncher.ui.screens.premiumScreen

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.awais.jsonlauncher.models.JsonProperty
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType
import com.revenuecat.purchases.ProductType
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.purchaseWith

@Composable
fun CustomPaywallContent(
    modifier: Modifier = Modifier,
    packages: List<Package>,
    onDismiss: () -> Unit,
    onPurchaseSuccess: () -> Unit
) {
    val context = LocalContext.current
    var selectedPackage by remember { mutableStateOf(packages.firstOrNull()) }
    var isPurchasing by remember { mutableStateOf(false) }


    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
//            .fillMaxSize()
            .padding(4.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Row {
            Spacer(modifier = Modifier.width(JsonSpacing.XS))

            Text("\"get_premium\"", color = JsonSyntax.colors.key)
            Text(":")

            Spacer(modifier = Modifier.width(JsonSpacing.SM))

            Text("{", color = JsonSyntax.colors.parenthesis)
        }
        Column(
            modifier = Modifier.padding(start = JsonSpacing.Indent),
        ) {
            Text(
                text = "/* Get unlimited access to all features */",
                color = JsonSyntax.colors.comment
            )

            Spacer(Modifier.height(12.dp))

            Row {
                Text("\"benefits\"", color = JsonSyntax.colors.key)
                Text(":")

                Spacer(modifier = Modifier.width(JsonSpacing.SM))

                Text("[", color = JsonSyntax.colors.parenthesis)
            }
            Column(
                modifier = Modifier.padding(start = JsonSpacing.Indent),
            ) {
                Text("\"access to all fonts\"", color = JsonSyntax.colors.string)
                Text("\"ability to add widgets\"", color = JsonSyntax.colors.string)
            }
            Row {
                Text("]", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }

            Row {
                Text("\"plans\"", color = JsonSyntax.colors.key)
                Text(":")

                Spacer(modifier = Modifier.width(JsonSpacing.SM))

                Text("[", color = JsonSyntax.colors.parenthesis)
            }
            Column(
                modifier = Modifier.padding(start = JsonSpacing.Indent),
            ) {
                packages.forEach { pkg ->
                    val isSelected = selectedPackage == pkg

                    PaywallPlanCard(
                        duration = if (pkg.product.type == ProductType.INAPP) "annual" else "yearly",
                        price = pkg.product.price.formatted,
                        billed = if (pkg.product.type == ProductType.INAPP) "once, forever" else "every 12 months",
                        isSelected = isSelected,
                        onClick = { selectedPackage = pkg }
                    )
                }
            }
            Row {
                Text("]", color = JsonSyntax.colors.parenthesis)
                Text(",")
            }


        }



        Spacer(modifier = Modifier.height(100.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedButton (
                onClick = {
                    val activity = context as? Activity ?: return@OutlinedButton
                    val targetPackage = selectedPackage ?: return@OutlinedButton
                    isPurchasing = true

                    Purchases.sharedInstance.purchaseWith(
                        PurchaseParams.Builder(activity, targetPackage).build(),
                        onError = { error, userCancelled ->
                            isPurchasing = false
                            if (!userCancelled) {
                                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                            }
                        },
                        onSuccess = { _, _ ->
                            isPurchasing = false
                            Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT)
                                .show()
                            onPurchaseSuccess()
                        }
                    )
                },
                enabled = selectedPackage != null && !isPurchasing,
                border = BorderStroke(1.dp, color = JsonSyntax.colors.parenthesis),
                modifier = Modifier
                    .width(300.dp)
                    .height(52.dp),
            ) {
                if (isPurchasing) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    val ctaText = selectedPackage?.let { pkg ->
                        val freeTrial = getFreeTrialText(pkg)
                        if (freeTrial != null) "Start Free Trial" else "Subscribe Now"
                    } ?: "Continue"
                    Text(text = ctaText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

        }

        Spacer(modifier = Modifier.width(JsonSpacing.RowHeight))

        Row {
            Spacer(modifier = Modifier.width(JsonSpacing.XS))
            Text("}", color = JsonSyntax.colors.parenthesis)
            Text(",")
        }
    }
}

private fun getFreeTrialText(pkg: Package): String? {
    val freePhase = pkg.product.defaultOption?.pricingPhases?.firstOrNull { it.price.amountMicros == 0L }
    return freePhase?.let {
        val period = it.billingPeriod
        "${period.value} ${period.unit.name.lowercase().removeSuffix("s")}${if (period.value > 1) "s" else ""} Free Trial"
    }
}