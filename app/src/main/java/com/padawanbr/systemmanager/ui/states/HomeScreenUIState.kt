package com.padawanbr.systemmanager.ui.states

import com.padawanbr.systemmanager.model.Manager

data class HomeScreenUIState(
  val sections: List<Manager> = emptyList(),
)