package com.padawanbr.systemmanager.managers


import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.RandomAccessFile
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

  fun calculateCpuScore(): Double {
    val cpuUsage = getCurrentCpuUsage() // Em porcentagem (0.0 a 100.0)
    val score = (100.0 - cpuUsage).coerceIn(0.0, 100.0)
    return score
  }

  private fun getCurrentCpuUsage(): Double {
    try {
      val reader = RandomAccessFile("/proc/stat", "r")
      val load1 = reader.readLine()
      val toks1 = load1.split("\\s+".toRegex())
      val idle1 = toks1[5].toLong()
      val cpu1 = toks1.subList(2, 9).map { it.toLong() }.sum()

      Thread.sleep(360)

      reader.seek(0)
      val load2 = reader.readLine()
      reader.close()

      val toks2 = load2.split("\\s+".toRegex())
      val idle2 = toks2[5].toLong()
      val cpu2 = toks2.subList(2, 9).map { it.toLong() }.sum()

      val cpuDelta = cpu2 - cpu1
      val idleDelta = idle2 - idle1

      val usage = ((cpuDelta - idleDelta) * 100.0) / cpuDelta

      return usage
    } catch (ex: Exception) {
      ex.printStackTrace()
      return 0.0
    }
  }

  private fun getMaxCpuFrequency(): Int {
    var maxFreq = 0
    try {
      for (i in 0 until Runtime.getRuntime().availableProcessors()) {
        val freqFile = File("/sys/devices/system/cpu/cpu$i/cpufreq/cpuinfo_max_freq")
        if (freqFile.exists()) {
          BufferedReader(FileReader(freqFile)).use { reader ->
            val line = reader.readLine()
            val freq = line.toIntOrNull() ?: 0
            if (freq > maxFreq) {
              maxFreq = freq
            }
          }
        }
      }
      // Converter de KHz para MHz
      return maxFreq / 1000
    } catch (e: Exception) {
      e.printStackTrace()
      return 0
    }
  }
}