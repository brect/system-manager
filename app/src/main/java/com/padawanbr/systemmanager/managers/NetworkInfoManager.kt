package com.padawanbr.systemmanager.managers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.CellInfoCdma
import android.telephony.CellInfoGsm
import android.telephony.CellInfoLte
import android.telephony.CellInfoNr
import android.telephony.CellInfoWcdma
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.net.Inet4Address
import java.net.NetworkInterface

class NetworkInfoManager(private val context: Context) {

  companion object {
    val REQUEST_CODE_LOCATION = 101
    val REQUEST_CODE_PHONE_STATE = 102
  }

  private fun checkAndRequestPermissions(): Boolean {
    val permissionsNeeded = mutableListOf<String>()

    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
      != PackageManager.PERMISSION_GRANTED
    ) {
      permissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_PHONE_STATE)
      != PackageManager.PERMISSION_GRANTED
    ) {
      permissionsNeeded.add(android.Manifest.permission.READ_PHONE_STATE)
    }

    if (permissionsNeeded.isNotEmpty()) {
      ActivityCompat.requestPermissions(
        context as Activity,
        permissionsNeeded.toTypedArray(),
        REQUEST_CODE_LOCATION // Você pode usar um código genérico aqui
      )

      return false
    } else {
      return true
    }
  }

  fun getNetworkInfo(): String {
    if (checkAndRequestPermissions()) {
      val connectionType = getConnectionType()
      val networkRate = getNetworkConnectionRate()
      val frequencyUsed = getFrequencyUsed()
      val networkQuality = getNetworkQuality()
      val ipAddress = getIPAddress()
      val carrierName = getCarrierName()
      val mobileNetworkType = getMobileNetworkType()

      val networkInfo = "Tipo de Conexão: $connectionType\n" +
          "Taxa de Conexão: $networkRate\n" +
          "Frequência Utilizada: $frequencyUsed\n" +
          "Qualidade da Rede: $networkQuality\n" +
          "Endereço IP: $ipAddress\n" +
          "Operadora: $carrierName\n" +
          "Tipo de Rede Móvel: $mobileNetworkType\n"

      Log.i("NetworkInfoManager", networkInfo)

      return networkInfo
    } else return "Necessário verificar permissões"
  }

  // Tipo de conexão
  fun getConnectionType(): String {
    val connectivityManager =
      context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val activeNetwork = connectivityManager.activeNetwork ?: return "Sem Conexão"
      val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        ?: return "Sem Conexão"

      when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Dados Móveis"
        else -> "Outro"
      }
    } else {
      val activeNetworkInfo = connectivityManager.activeNetworkInfo ?: return "Sem Conexão"
      if (activeNetworkInfo.isConnected) {
        when (activeNetworkInfo.type) {
          ConnectivityManager.TYPE_WIFI -> "Wi-Fi"
          ConnectivityManager.TYPE_MOBILE -> "Dados Móveis"
          else -> "Outro"
        }
      } else {
        "Sem Conexão"
      }
    }
  }

  // Taxa de conexão da rede
  fun getNetworkConnectionRate(): String {
    val connectionType = getConnectionType()
    return when (connectionType) {
      "Wi-Fi" -> {
        val wifiManager =
          context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val linkSpeed = wifiInfo.linkSpeed // Mbps
        if (linkSpeed != -1) {
          "$linkSpeed Mbps"
        } else {
          "Não disponível"
        }
      }

      "Dados Móveis" -> {
        getMobileNetworkType()
      }

      else -> "Não disponível"
    }
  }

  // Frequência utilizada
  fun getFrequencyUsed(): String {
    val connectionType = getConnectionType()
    return if (connectionType == "Wi-Fi") {
      val wifiManager =
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
      val wifiInfo = wifiManager.connectionInfo
      val frequency = wifiInfo.frequency // MHz
      if (frequency != -1) {
        "$frequency MHz"
      } else {
        "Não disponível"
      }
    } else {
      "Não disponível"
    }
  }

  // Qualidade da rede
  fun getNetworkQuality(): String {
    val connectionType = getConnectionType()
    return when (connectionType) {
      "Wi-Fi" -> {
        val wifiManager =
          context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val rssi = wifiInfo.rssi
        val level = WifiManager.calculateSignalLevel(rssi, 5)
        "Sinal Wi-Fi: $level/4"
      }

      "Dados Móveis" -> {
        if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
          ) == PackageManager.PERMISSION_GRANTED
        ) {
          val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
          val cellInfoList = telephonyManager.allCellInfo
          if (!cellInfoList.isNullOrEmpty()) {
            val cellInfo = cellInfoList[0]
            val signalStrength = when (cellInfo) {
              is CellInfoGsm -> cellInfo.cellSignalStrength
              is CellInfoCdma -> cellInfo.cellSignalStrength
              is CellInfoLte -> cellInfo.cellSignalStrength
              is CellInfoWcdma -> cellInfo.cellSignalStrength
              is CellInfoNr -> cellInfo.cellSignalStrength
              else -> null
            }
            signalStrength?.let {
              val level = it.level
              "Sinal Móvel: $level/4"
            } ?: "Não disponível"
          } else {
            "Não disponível"
          }
        } else {
          "Permissão necessária"
        }
      }

      else -> "Não disponível"
    }
  }

  // Endereço IP
  fun getIPAddress(): String {
    try {
      val interfaces = NetworkInterface.getNetworkInterfaces()
      for (intf in interfaces) {
        val addresses = intf.inetAddresses
        for (addr in addresses) {
          if (!addr.isLoopbackAddress && addr is Inet4Address) {
            return addr.hostAddress ?: "Não disponível"
          }
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
    return "Não disponível"
  }

  // Nome da operadora
  fun getCarrierName(): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    return telephonyManager.networkOperatorName ?: "Desconhecido"
  }

  // Tipo de rede móvel detalhado
  fun getMobileNetworkType(): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    val networkType = telephonyManager.networkType
    return when (networkType) {
      TelephonyManager.NETWORK_TYPE_GPRS -> "GPRS"
      TelephonyManager.NETWORK_TYPE_EDGE -> "EDGE"
      TelephonyManager.NETWORK_TYPE_CDMA -> "CDMA"
      TelephonyManager.NETWORK_TYPE_1xRTT -> "1xRTT"
      TelephonyManager.NETWORK_TYPE_IDEN -> "iDen"
      TelephonyManager.NETWORK_TYPE_UMTS -> "UMTS"
      TelephonyManager.NETWORK_TYPE_EVDO_0 -> "EVDO rev. 0"
      TelephonyManager.NETWORK_TYPE_EVDO_A -> "EVDO rev. A"
      TelephonyManager.NETWORK_TYPE_HSDPA -> "HSDPA"
      TelephonyManager.NETWORK_TYPE_HSUPA -> "HSUPA"
      TelephonyManager.NETWORK_TYPE_HSPA -> "HSPA"
      TelephonyManager.NETWORK_TYPE_EVDO_B -> "EVDO rev. B"
      TelephonyManager.NETWORK_TYPE_EHRPD -> "eHRPD"
      TelephonyManager.NETWORK_TYPE_HSPAP -> "HSPA+"
      TelephonyManager.NETWORK_TYPE_LTE -> "LTE"
      TelephonyManager.NETWORK_TYPE_NR -> "NR (5G)"
      else -> "Desconhecido"
    }
  }

  fun calculateNetworkScore(): Double {
    val networkType = getConnectionType()
    return when (networkType) {
      "Wi-Fi" -> 100.0
      "Dados Móveis" -> 70.0
      "Sem Conexão" -> 0.0
      else -> 50.0
    }
  }
}




//  override fun onRequestPermissionsResult(
//    requestCode: Int,
//    permissions: Array<String>,
//    grantResults: IntArray
//  ) {
//    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    when (requestCode) {
//      REQUEST_CODE_LOCATION -> {
//        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//          // Permissão concedida, chame o método novamente
//          val networkQuality = networkInfoManager.getNetworkQuality()
//          // Atualize a interface do usuário com o resultado
//        } else {
//          // Permissão negada, informe ao usuário que a permissão é necessária
//          Toast.makeText(
//            this,
//            "Permissão de localização é necessária para obter a qualidade da rede.",
//            Toast.LENGTH_SHORT
//          ).show()
//        }
//      }
//
//      REQUEST_CODE_PHONE_STATE -> {
//        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//          // Permissão concedida, chame os métodos novamente
//          val carrierName = networkInfoManager.getCarrierName()
//          val mobileNetworkType = networkInfoManager.getMobileNetworkType()
//          // Atualize a interface do usuário com o resultado
//        } else {
//          // Permissão negada, informe ao usuário que a permissão é necessária
//          Toast.makeText(
//            this,
//            "Permissão de estado do telefone é necessária para obter informações da operadora.",
//            Toast.LENGTH_SHORT
//          ).show()
//        }
//      }
//    }
//  }