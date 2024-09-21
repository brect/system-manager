package com.padawanbr.systemmanager.managers


import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager
import kotlin.math.pow
import kotlin.math.sqrt

class DeviceInfoManager(private val context: Context) {

  fun deviceInfo(): Manager {
    val deviceInfo = Manager(
      title = "Device Info",
      items = listOf(
        Item("DEVICE ID", getAndroidId(context)),
        Item("MODELO", Build.MODEL),
        Item("FABRICANTE", Build.MANUFACTURER),
        Item("HARDWARE", Build.HARDWARE),
        Item("DEVICE", Build.DEVICE),
        Item("VERSÃO ANDROID", Build.VERSION.RELEASE),
        Item("SDK", Build.VERSION.SDK_INT.toString()),
        Item("DISPLAY", Build.DISPLAY),
        Item("HOST", Build.HOST),
//        Item("CPU_ABI2", Build.CPU_ABI2)
      )
    )

    Log.i("SystemDeviceManager", "device info: \n${deviceInfo}")

    return deviceInfo
  }


  fun getAndroidId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
  }

  fun screenInfo(): Manager {
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

    val screenInfos = Manager(
      title = "Screen Info",
      items = listOf(
        Item("RESOLUÇÃO TELA", "${widthPixels} x ${heightPixels} pixels"),
        Item("DENSIDADE DPI", "${densityDpi} dpi"),
        Item("DENSIDADE", "$density"),
        Item("TAMANHO EM POLEGADAS", "${"%.2f".format(screenSizeInches)} polegadas"),
        Item("REFRESH RATE", "${floatToString(refreshRate)} Hz")
      )
    )

    Log.i("SystemDeviceManager", "screen info: ${screenInfos}")

    return screenInfos
  }

  fun floatToString(value: Float): String {
    return "%.2f".format(value)
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