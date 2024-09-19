package com.padawanbr.systemmanager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager
import com.padawanbr.systemmanager.ui.components.CardManagerSection
import com.padawanbr.systemmanager.ui.states.HomeScreenUIState
import com.padawanbr.systemmanager.ui.theme.SystemManagerTheme
import com.padawanbr.systemmanager.ui.viewmodels.HomeScreenViewModel

@Composable
fun HomeScreen(viewModel: HomeScreenViewModel){
  val state by viewModel.uiState.collectAsState()
  HomeScreen(state)
}

@Composable
fun HomeScreen(
  state: HomeScreenUIState = HomeScreenUIState()
) {
  Column {
    LazyColumn(
      Modifier
        .fillMaxSize().padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(bottom = 16.dp)
    ) {
      items(state.sections) { section ->
        CardManagerSection(
          manager = section
        )
      }
    }
  }
}

@Preview
@Composable
private fun HomeScreenPreview() {
  SystemManagerTheme {
    HomeScreen(HomeScreenUIState(sampleManagers))
  }
}

val sampleManagers: List<Manager> = listOf(
  Manager(
    title = "CPU Info",
    listOf(
      Item(key = "Key", value = "Value"),
      Item(key = "Key", value = "Value"),
    )
  ),
  Manager(
    title = "GPU Info",
    listOf(
      Item(key = "Key", value = "Value"),
      Item(key = "Key", value = "Value"),
    )
  ),
  Manager(
    title = "CPU Info",
    listOf(
      Item(key = "Key", value = "Value"),
      Item(key = "Key", value = "Value"),
    )
  )
)