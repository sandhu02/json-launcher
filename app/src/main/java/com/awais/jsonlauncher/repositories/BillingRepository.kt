package com.awais.jsonlauncher.repositories

import android.util.Log
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.awaitCustomerInfo
import com.revenuecat.purchases.awaitOfferings
import com.revenuecat.purchases.awaitRestore
import jakarta.inject.Inject
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration.Companion.milliseconds

class BillingRepository @Inject constructor() {

    companion object {
        private const val TAG = "BillingRepository"
    }

    suspend fun isPremium(): Boolean {
        if (!Purchases.isConfigured) {
            Log.w(TAG, "isPremium: Purchases is not configured")
            return false
        }
        return try {
            Log.d(TAG, "isPremium: Fetching customer info...")
            val customerInfo = withTimeout(5000L.milliseconds) {
                Purchases.sharedInstance.awaitCustomerInfo()
            }
            val isActive = customerInfo.entitlements["json_launcher_premium"]?.isActive == true
            Log.d(TAG, "isPremium result: $isActive")
            isActive
        } catch (e: Exception) {
            Log.e(TAG, "isPremium error or timeout", e)
            false
        }
    }

    suspend fun restorePurchases(): Boolean {
        if (!Purchases.isConfigured) return false
        return try {
            val customerInfo = withTimeout(5000L.milliseconds) {
                Purchases.sharedInstance.awaitRestore()
            }
            customerInfo.entitlements["json_launcher_premium"]?.isActive == true
        } catch (_: Exception) {
            false
        }
    }

    suspend fun getOffering(): Result<Offering?> {
        if (!Purchases.isConfigured) {
            return Result.failure(IllegalStateException("RevenueCat is not configured"))
        }
        return try {
            Log.d(TAG, "getOffering: Fetching offerings from RevenueCat & Google Play...")
            val currentOffering = withTimeout(5000L.milliseconds) {
                Purchases.sharedInstance.awaitOfferings().current
            }
            Log.d(TAG, "getOffering result: offering=${currentOffering?.identifier}, packages=${currentOffering?.availablePackages?.size}, hasPaywall=${currentOffering?.hasPaywall}")
            Result.success(currentOffering)
        } catch (e: Exception) {
            Log.e(TAG, "getOffering error or timeout", e)
            Result.failure(e)
        }
    }

}