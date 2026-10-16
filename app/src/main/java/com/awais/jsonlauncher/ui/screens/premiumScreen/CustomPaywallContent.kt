package com.awais.jsonlauncher.ui.screens.premiumScreen

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.PackageType
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
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Unlock Premium",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Get unlimited access to all features",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            packages.forEach { pkg ->
                val isSelected = selectedPackage == pkg
                val title = getPackageTitle(pkg)
                val subtitle = getPackageSubtitle(pkg)
                val freeTrialText = getFreeTrialText(pkg)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { selectedPackage = pkg }
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                if (pkg.packageType == PackageType.ANNUAL || pkg.packageType == PackageType.LIFETIME) {
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                            .padding(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (pkg.packageType == PackageType.LIFETIME) "BEST VALUE" else "POPULAR",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            Text(
                                text = pkg.product.price.formatted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = subtitle,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    val activity = context as? Activity ?: return@Button
                    val targetPackage = selectedPackage ?: return@Button
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
                            Toast.makeText(context, "Purchase successful!", Toast.LENGTH_SHORT).show()
                            onPurchaseSuccess()
                        }
                    )
                },
                enabled = selectedPackage != null && !isPurchasing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp)
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

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close")
            }
        }
    }
}

private fun getPackageTitle(pkg: Package): String {
    return when (pkg.packageType) {
        PackageType.ANNUAL -> "Annual Pass"
        PackageType.MONTHLY -> "Monthly Pass"
        PackageType.LIFETIME -> "Lifetime Access"
        PackageType.SIX_MONTH -> "6-Month Pass"
        PackageType.THREE_MONTH -> "3-Month Pass"
        PackageType.TWO_MONTH -> "2-Month Pass"
        PackageType.WEEKLY -> "Weekly Pass"
        else -> pkg.product.title.substringBefore("(").trim().ifEmpty { pkg.identifier }
    }
}

private fun getPackageSubtitle(pkg: Package): String {
    val priceFormatted = pkg.product.price.formatted
    return when (pkg.packageType) {
        PackageType.LIFETIME -> "One-time payment • Pay once, keep forever"
        PackageType.ANNUAL -> {
            val monthlyPrice = pkg.product.formattedPricePerMonth()
            if (monthlyPrice != null) {
                "$monthlyPrice / month • Billed annually at $priceFormatted"
            } else {
                "Billed annually at $priceFormatted"
            }
        }
        PackageType.MONTHLY -> "Billed monthly at $priceFormatted"
        PackageType.WEEKLY -> "Billed weekly at $priceFormatted"
        else -> {
            if (pkg.product.description.isNotBlank()) pkg.product.description else "Billed at $priceFormatted"
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