package com.awais.jsonlauncher.repositories

import android.util.Log
import com.revenuecat.purchases.Offering
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.awaitCustomerInfo
import com.revenuecat.purchases.awaitOfferings
import com.revenuecat.purchases.awaitRestore
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeoutOrNull
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
            val result = withTimeoutOrNull(5000L.milliseconds) {
                coroutineScope {
                    async(Dispatchers.IO) {
                        val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
                        customerInfo.entitlements["json_launcher_premium"]?.isActive == true
                    }.await()
                }
            }
            val isActive = result == true
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
            val result = withTimeoutOrNull(5000L.milliseconds) {
                coroutineScope {
                    async(Dispatchers.IO) {
                        val customerInfo = Purchases.sharedInstance.awaitRestore()
                        customerInfo.entitlements["json_launcher_premium"]?.isActive == true
                    }.await()
                }
            }
            result == true
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
            val currentOffering = withTimeoutOrNull(6000L.milliseconds) {
                coroutineScope {
                    async(Dispatchers.IO) {
                        Purchases.sharedInstance.awaitOfferings().current
                    }.await()
                }
            }
            if (currentOffering == null) {
                Log.w(TAG, "getOffering: Google Play Billing service timed out or is deadlocked")
                Result.failure(IllegalStateException("Google Play Billing service timed out or is deadlocked on this device. Please force stop Google Play Store / restart device."))
            } else {
                Log.d(TAG, "getOffering result: offering=${currentOffering.identifier}, packages=${currentOffering.availablePackages.size}, hasPaywall=${currentOffering.hasPaywall}")
                Result.success(currentOffering)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getOffering error or timeout", e)
            Result.failure(e)
        }
    }

}