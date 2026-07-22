//package com.awais.jsonlauncher.ui.jsonObject
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import com.awais.jsonlauncher.models.AppInfo
//import com.awais.jsonlauncher.ui.home.HomeScreenUiState
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.update
//
//data class JsonItemUiState(
//    val isCollapsed: Boolean = false
//)
//
//
//class JsonItemViewModel() : ViewModel() {
//    private val _uiState = MutableStateFlow(JsonItemUiState())
//    val uiState = _uiState.asStateFlow()
//
//    fun onCollapseClick() {
////        Log.d("Json Item" , "${uiState.value.isCollapsed}")
//        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
//    }
//}