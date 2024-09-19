package com.padawanbr.systemmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.padawanbr.systemmanager.managers.InfoManagers
import com.padawanbr.systemmanager.ui.screens.HomeScreen
import com.padawanbr.systemmanager.ui.screens.sampleManagers
import com.padawanbr.systemmanager.ui.states.HomeScreenUIState
import com.padawanbr.systemmanager.ui.theme.SystemManagerTheme
import com.padawanbr.systemmanager.ui.viewmodels.HomeScreenViewModel
import com.padawanbr.systemmanager.ui.viewmodels.HomeScreenViewModelFactory

class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SystemManagerApp {
        val viewModel by viewModels<HomeScreenViewModel>(
          factoryProducer = {
            HomeScreenViewModelFactory(InfoManagers(this))
          }
        )
        HomeScreen(viewModel)
      }
    }
  }
}

@Composable
fun SystemManagerApp(content: @Composable () -> Unit = {}) {
  SystemManagerTheme {
    Surface {
      Surface {
        Scaffold { paddingValues ->
          Box(modifier = Modifier.padding(paddingValues)) {
            content()
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SystemManagerAppPreview() {
  SystemManagerTheme {
    SystemManagerApp {
      HomeScreen(HomeScreenUIState(sampleManagers))
    }
  }
}