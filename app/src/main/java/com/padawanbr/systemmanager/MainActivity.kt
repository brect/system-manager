package com.padawanbr.systemmanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.padawanbr.systemmanager.ui.theme.SystemManagerTheme

class MainActivity : ComponentActivity() {

  val networkCallback = object : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
      // Rede disponível
      Log.i("System NetworkManager", "onAvailable: $network")
    }

    override fun onLost(network: Network) {
      // Rede perdida
      Log.i("System NetworkManager", "onLost: $network")
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
      // Mudança nas capacidades da rede (ex: nível do sinal)
      val signalStrength = networkCapabilities.signalStrength

      Log.i("System NetworkManager", "onCapabilitiesChanged: $network")
      Log.i("System NetworkManager", "signalStrength: $signalStrength")
    }
  }

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

    val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    connectivityManager.registerDefaultNetworkCallback(networkCallback)

    val wifiManager = baseContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    val wifiInfo = wifiManager.connectionInfo
    val rssi = wifiInfo.rssi // Nível do sinal em dBm
    Log.i("System NetworkManager", "rssi: $rssi")

    val activeNetwork = connectivityManager?.activeNetwork
    val networkCapabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
    val isWifi = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
    val isCellular = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

    Log.i("System NetworkManager", "isWifi: $isWifi")
    Log.i("System NetworkManager", "isCellular: $isCellular")
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