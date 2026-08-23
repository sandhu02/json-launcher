package com.awais.jsonlauncher.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.awais.jsonlauncher.repositories.AppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//data class LauncherSettingsUiState(
//
//)

@HiltViewModel
class LauncherSettingsViewModel @Inject constructor(
    private val repository: AppsRepository
) : ViewModel() {

}