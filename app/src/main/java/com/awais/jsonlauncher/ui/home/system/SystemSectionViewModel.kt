package com.awais.jsonlauncher.ui.home.system

import android.content.Context
import android.os.BatteryManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.NotificationInfo
import com.awais.jsonlauncher.repositories.BatteryRepository
import com.awais.jsonlauncher.repositories.NetworkRepository
import com.awais.jsonlauncher.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class SystemSectionUiState(
    val time: String = "",
    val date: String = "",
    val battery: Int = 0,
    val isCharging: Boolean = false,
    val networkState: Boolean = false,
    val flashLightState: Boolean = false,
    val notifications: List<NotificationInfo> = emptyList(),
    val isCollapsed: Boolean = false
)

@HiltViewModel
class SystemSectionViewModel @Inject constructor(
    private val batteryRepository: BatteryRepository,
    private val networkRepository: NetworkRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(SystemSectionUiState())
    val uiState = _uiState.asStateFlow()

    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }

    private fun updateDateTime() {
        viewModelScope.launch {
            while (true) {
                val time = LocalTime.now()
                val formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                val date = LocalDate.now()
                val formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy"))
                
                val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
                val battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
                val status = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_STATUS)
                val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || 
                                 status == BatteryManager.BATTERY_STATUS_FULL

                _uiState.update {
                    it.copy(
                        time = formattedTime,
                        date = formattedDate,
                        battery = battery,
                        isCharging = isCharging
                    )
                }
                delay(1000)
            }
        }
    }

    private fun updateNetworkState() {
        viewModelScope.launch {
            networkRepository.networkState().collect { connected ->
                _uiState.update { it.copy(networkState = connected.connectionStatus) }
            }
        }
    }

    init {
        updateDateTime()
        updateNetworkState()
    }
}
