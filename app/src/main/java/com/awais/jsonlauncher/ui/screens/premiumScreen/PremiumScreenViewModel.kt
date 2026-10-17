package com.awais.jsonlauncher.ui.screens.premiumScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.repositories.BillingRepository
import com.revenuecat.purchases.Offering
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

data class PremiumScreenUiState(
    val isPremium: Boolean = false,
    val isLoading: Boolean = true,
    val offering: Offering? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class PremiumScreenViewModel @Inject constructor(
    private val billingRepository: BillingRepository
) : ViewModel() {

    companion object {
        private const val TAG = "PremiumScreenViewModel"
    }

    private val _uiState = MutableStateFlow(PremiumScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun checkPremiumStatus() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            try {
                withTimeout(20.seconds) {
                    Log.d(TAG, "Checking premium status...")
                    val isPremium = billingRepository.isPremium()
                    if (isPremium) {
                        Log.d(TAG, "User is premium")
                        _uiState.update { it.copy(isPremium = true, isLoading = false) }
                    } else {
                        Log.d(TAG, "User is not premium, fetching offering...")
                        billingRepository.getOffering().fold(
                            onSuccess = { offering ->
                                Log.d(TAG, "Offering fetched: ${offering?.identifier}")
                                val errorMessage = when {
                                    offering == null -> "No current offering found in RevenueCat Dashboard."
                                    offering.availablePackages.isEmpty() -> "Offering found, but Google Play Store returned no product details for (yearly_subscription, lifetime_purchase). Ensure products are active in Google Play Console and testing account is configured."
                                    !offering.hasPaywall -> "Offering found, but RevenueCat SDK returned no Paywall configuration for it (offering.hasPaywall is false).\n\nIf you recently published the Paywall in RevenueCat Dashboard, clear app data or reinstall the app to force RevenueCat to clear its HTTP 304 cache."
                                    else -> null
                                }
                                _uiState.update {
                                    it.copy(
                                        isPremium = false,
                                        offering = offering,
                                        isLoading = false,
                                        errorMessage = errorMessage
                                    )
                                }
                            },
                            onFailure = { error ->
                                Log.e(TAG, "Failed to get offering", error)
                                _uiState.update {
                                    it.copy(
                                        isPremium = false,
                                        isLoading = false,
                                        errorMessage = error.localizedMessage ?: "Failed to load offerings."
                                    )
                                }
                            }
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Timeout or error in checkPremiumStatus", e)
                _uiState.update {
                    it.copy(
                        isPremium = false,
                        isLoading = false,
                        errorMessage = "Request timed out connecting to RevenueCat (${e.localizedMessage ?: "Timeout"}). Check your network connection."
                    )
                }
            }
        }
    }

    init {
        checkPremiumStatus()
    }

}