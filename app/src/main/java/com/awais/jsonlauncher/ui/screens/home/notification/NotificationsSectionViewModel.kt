package com.awais.jsonlauncher.ui.screens.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.NotificationInfo
import com.awais.jsonlauncher.repositories.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationsSectionUiState(
    val notifications: List<NotificationInfo> = emptyList(),
    val isCollapsed: Boolean = false
)

@HiltViewModel
class NotificationsSectionViewModel @Inject constructor(
    private val repository: NotificationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsSectionUiState())
    val uiState = _uiState.asStateFlow()

    fun updateNotifications() {
        viewModelScope.launch {
            repository.notifications.collect { notifications ->
                _uiState.update { it.copy(notifications = notifications) }
            }
        }
    }

    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }

    fun onNotificationCollapseClick(notificationKey: String) {
        _uiState.update { state ->
            state.copy(
                notifications = state.notifications.map { notification ->
                    if (notification.key == notificationKey)
                        notification.copy(isCollapsed = !notification.isCollapsed)
                    else
                        notification
                }
            )
        }
    }

    init {
        updateNotifications()
    }
}
