package com.padawanbr.systemmanager.managers


import android.content.Context
import android.os.Build
import android.util.Log
import android.view.WindowManager
import kotlin.math.pow
import kotlin.math.sqrt

class DeviceInfoManager(private val context: Context) {

  fun deviceInfo(): String {
    val deviceInfo = "MODELO: ${Build.MODEL}\n" +
        "FABRICANTE: ${Build.MANUFACTURER}\n" +
        "HARDWARE: ${Build.HARDWARE}\n" +
        "DEVICE: ${Build.DEVICE}\n" +
        "VERSÃO ANDROID: ${Build.VERSION.RELEASE}\n" +
        "SDK: ${Build.VERSION.SDK_INT}\n" +
        "DISPLAY: ${Build.DISPLAY}\n" +
        "HOST: ${Build.HOST}\n" +
        "CPU_ABI2: ${Build.CPU_ABI2}\n"

    Log.i("SystemDeviceManager", "device info: \n${deviceInfo}")

    return deviceInfo
  }

  fun screenInfo(): String {
    val displayMetrics = context.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    val heightPixels = displayMetrics.heightPixels
    val densityDpi = displayMetrics.densityDpi
    val density = displayMetrics.density
    val xdpi = displayMetrics.xdpi
    val ydpi = displayMetrics.ydpi

    val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    val refreshRate = display.refreshRate

    val screenSizeInches = getScreenSize(widthPixels, xdpi, heightPixels, ydpi)

    val screenInfos = "RESOLUÇÃO TELA: ${widthPixels} x ${heightPixels} pixels\n" +
        "DENSIDADE DPI: ${densityDpi} dpi\n" +
        "DENSIDADE: ${density}\n" +
        "TAMANHO EM POLEGADAS: ${"%.2f".format(screenSizeInches)} polegadas\n" +
        "REFRESH RATE: ${refreshRate} Hz\n"

    Log.i("SystemDeviceManager", "screen info: ${screenInfos}")

    return screenInfos
  }

  private fun getScreenSize(
    widthPixels: Int,
    xdpi: Float,
    heightPixels: Int,
    ydpi: Float
  ): Float {
    val widthInches = widthPixels / xdpi
    val heightInches = heightPixels / ydpi
    val screenSizeInches = sqrt(widthInches.pow(2) + heightInches.pow(2))
    return screenSizeInches
  }
}