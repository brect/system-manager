package com.padawanbr.systemmanager.managers

import android.os.Build
import android.util.Log

class DeviceInfoManager {

  fun deviceInfo(): String {

    val deviceInfo = "Modelo: ${Build.MODEL}\n" +
        "Fabricante: ${Build.MANUFACTURER}\n" +
        "Versão do Android: ${Build.VERSION.RELEASE}\n" +
        "SDK: ${Build.VERSION.SDK_INT}\n" +
        "DISPLAY: ${Build.DISPLAY}\n" +
        "DEVICE: ${Build.DEVICE}\n" +
        "HARDWARE: ${Build.HARDWARE}\n" +
        "HOST: ${Build.HOST}\n" +
        "CPU_ABI2: ${Build.CPU_ABI2}\n"

    Log.i("SystemDeviceManager", "device info: ${deviceInfo}")

    return deviceInfo
  }
}