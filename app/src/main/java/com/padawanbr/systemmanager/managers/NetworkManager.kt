package com.padawanbr.systemmanager.managers

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class NetworkManager(val context: Context) {

  val connectivityManager = context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
//  connectivityManager.registerDefaultNetworkCallback(networkCallback)

  val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
  val wifiInfo = wifiManager.connectionInfo
  val rssi = wifiInfo.rssi // Nível do sinal em dBm
//  Log.i("System NetworkManager", "rssi: $rssi")

  val activeNetwork = connectivityManager?.activeNetwork
  val networkCapabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
  val isWifi = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
  val isCellular = networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ?: false

//  Log.i("System NetworkManager", "isWifi: $isWifi")
//  Log.i("System NetworkManager", "isCellular: $isCellular")
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

}