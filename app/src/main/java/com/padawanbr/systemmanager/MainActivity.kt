package com.padawanbr.systemmanager

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.padawanbr.systemmanager.managers.DeviceBateryManager
import com.padawanbr.systemmanager.managers.DeviceInfoManager
import com.padawanbr.systemmanager.managers.GpuInfoManager
import com.padawanbr.systemmanager.managers.MemoryManager
import com.padawanbr.systemmanager.managers.NetworkInfoManager
import com.padawanbr.systemmanager.managers.NetworkInfoManager.Companion.REQUEST_CODE_LOCATION
import com.padawanbr.systemmanager.managers.NetworkInfoManager.Companion.REQUEST_CODE_PHONE_STATE
import com.padawanbr.systemmanager.managers.StorageInfoManager
import com.padawanbr.systemmanager.ui.theme.SystemManagerTheme

class MainActivity : ComponentActivity() {

  val memoryManager by lazy { MemoryManager(this) }
  val deviceInfoManager = DeviceInfoManager(this)
  val deviceBateryManager = DeviceBateryManager(this)
  val gpuInfoManager = GpuInfoManager()
  val storageInfoManager = StorageInfoManager()
  val networkInfoManager = NetworkInfoManager(this)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      SystemManagerTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          Greeting(
            name = "Android",
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    memoryManager.subscribe()
    memoryManager.runCheckerMemory()

    deviceInfoManager.deviceInfo()
    deviceInfoManager.screenInfo()
    deviceBateryManager.logBateryInfo()

    gpuInfoManager.gpuInfo()

    storageInfoManager.getStorageInfo()

    networkInfoManager.getNetworkInfo()

  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
      REQUEST_CODE_LOCATION -> {
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
          // Permissão concedida, chame o método novamente
          val networkQuality = networkInfoManager.getNetworkQuality()
          // Atualize a interface do usuário com o resultado
        } else {
          // Permissão negada, informe ao usuário que a permissão é necessária
          Toast.makeText(this, "Permissão de localização é necessária para obter a qualidade da rede.", Toast.LENGTH_SHORT).show()
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
          Toast.makeText(this, "Permissão de estado do telefone é necessária para obter informações da operadora.", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
  Text(
    text = "Hello $name!",
    modifier = modifier
  )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
  SystemManagerTheme {
    Greeting("Android")
  }
}