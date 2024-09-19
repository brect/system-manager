package com.padawanbr.systemmanager.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.padawanbr.systemmanager.managers.InfoManagers

class HomeScreenViewModelFactory(private val infoManagers: InfoManagers) : ViewModelProvider.Factory {

  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
      return HomeScreenViewModel(infoManagers) as T
    }
    throw IllegalArgumentException("ViewModel desconhecido")
  }
}