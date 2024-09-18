package com.padawanbr.systemmanager

import android.os.Bundle
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
import com.padawanbr.systemmanager.managers.DeviceBateryManager
import com.padawanbr.systemmanager.managers.DeviceInfoManager
import com.padawanbr.systemmanager.managers.GpuInfoManager
import com.padawanbr.systemmanager.managers.MemoryManager
import com.padawanbr.systemmanager.managers.StorageInfoManager
import com.padawanbr.systemmanager.ui.theme.SystemManagerTheme

class MainActivity : ComponentActivity() {

  val memoryManager by lazy { MemoryManager(this) }
  val deviceInfoManager = DeviceInfoManager(this)
  val deviceBateryManager = DeviceBateryManager(this)
  val gpuInfoManager = GpuInfoManager()
  val storageInfoManager = StorageInfoManager()

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