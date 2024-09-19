package com.padawanbr.systemmanager

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.padawanbr.systemmanager.managers.DeviceBateryManager
import com.padawanbr.systemmanager.managers.DeviceInfoManager
import com.padawanbr.systemmanager.managers.GpuInfoManager
import com.padawanbr.systemmanager.managers.InfoManagers
import com.padawanbr.systemmanager.managers.MemoryManager
import com.padawanbr.systemmanager.managers.NetworkInfoManager
import com.padawanbr.systemmanager.managers.NetworkInfoManager.Companion.REQUEST_CODE_LOCATION
import com.padawanbr.systemmanager.managers.NetworkInfoManager.Companion.REQUEST_CODE_PHONE_STATE
import com.padawanbr.systemmanager.managers.StorageInfoManager
import com.padawanbr.systemmanager.ui.screens.HomeScreen
import com.padawanbr.systemmanager.ui.screens.sampleManagers
import com.padawanbr.systemmanager.ui.states.HomeScreenUIState
import com.padawanbr.systemmanager.ui.theme.SystemManagerTheme
import com.padawanbr.systemmanager.ui.viewmodels.HomeScreenViewModel
import com.padawanbr.systemmanager.ui.viewmodels.HomeScreenViewModelFactory

class MainActivity : ComponentActivity() {

  val networkInfoManager = NetworkInfoManager(this)

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

  override fun onResume() {
    super.onResume()
//    memoryManager.subscribe()
//    memoryManager.runCheckerMemory()

//    deviceInfoManager.deviceInfo()
//    deviceInfoManager.screenInfo()
//    deviceBateryManager.logBateryInfo()
//
//    gpuInfoManager.gpuInfo()
//
//    storageInfoManager.getStorageInfo()

    networkInfoManager.getNetworkInfo()

  }

  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      REQUEST_CODE_LOCATION -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          // Permissão concedida, chame o método novamente
          val networkQuality = networkInfoManager.getNetworkQuality()
          // Atualize a interface do usuário com o resultado
        } else {
          // Permissão negada, informe ao usuário que a permissão é necessária
          Toast.makeText(
            this,
            "Permissão de localização é necessária para obter a qualidade da rede.",
            Toast.LENGTH_SHORT
          ).show()
        }
      }

      REQUEST_CODE_PHONE_STATE -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          // Permissão concedida, chame os métodos novamente
          val carrierName = networkInfoManager.getCarrierName()
          val mobileNetworkType = networkInfoManager.getMobileNetworkType()
          // Atualize a interface do usuário com o resultado
        } else {
          // Permissão negada, informe ao usuário que a permissão é necessária
          Toast.makeText(
            this,
            "Permissão de estado do telefone é necessária para obter informações da operadora.",
            Toast.LENGTH_SHORT
          ).show()
        }
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