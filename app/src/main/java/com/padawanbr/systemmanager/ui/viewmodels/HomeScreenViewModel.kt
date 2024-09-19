package com.padawanbr.systemmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padawanbr.systemmanager.managers.InfoManagers
import com.padawanbr.systemmanager.model.Manager
import com.padawanbr.systemmanager.ui.states.HomeScreenUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenViewModel(val managers: InfoManagers) : ViewModel() {

  private val infoManagers = MutableStateFlow(managers).asStateFlow()

  private val _uiState = MutableStateFlow(HomeScreenUIState())
  val uiState = _uiState.asStateFlow()

  init {
    viewModelScope.launch {

      infoManagers.collect { infos ->
        _uiState.update {
          it.copy(infos.managers)
        }
      }
    }
  }
}