package com.padawanbr.systemmanager.managers

import android.app.Activity.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager


class MemoryManager(context: Context) {

  val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

  val memoryInfo by lazy { ActivityManager.MemoryInfo() }

  fun subscribe() {
    activityManager.getMemoryInfo(memoryInfo)
  }

  fun memoryInfo(): Manager {

    subscribe()

    val totalMemory = memoryInfo.totalMem // Memória RAM total do dispositivo
    val availableMemory = memoryInfo.availMem // Memória RAM disponível
    val threshold = memoryInfo.threshold // Limite de uso de memória
    val lowMemory = memoryInfo.lowMemory // Indica se a memória RAM está baixa
    val usedMemory = totalMemory - availableMemory // Memória RAM utilizada
    val appMemoryInfo = getAppMemoryInfo() //uso de memória do aplicativo

    val memoryInfo = Manager(
      title = "Informações de Memória",
      items = listOf(
        // Informações gerais
        Item("TOTAL_MEMORY", "${formatBytesToMB(totalMemory)} MB"),
        Item("AVAILABLE_MEMORY", "${formatBytesToMB(availableMemory)} MB"),
        Item("USED_MEMORY", "${formatBytesToMB(usedMemory)} MB"),
        Item("THRESHOLD", "${formatBytesToMB(threshold)} MB"),
        Item("LOW_MEMORY", if (lowMemory) "Sim" else "Não"),
        // Informações do aplicativo
        Item("APP_TOTAL_PSS", "${formatKBToMB(appMemoryInfo.totalPss)} MB"),
        Item("APP_DALVIK_PSS", "${formatKBToMB(appMemoryInfo.dalvikPss)} MB"),
        Item("APP_NATIVE_PSS", "${formatKBToMB(appMemoryInfo.nativePss)} MB"),
        Item("APP_OTHER_PSS", "${formatKBToMB(appMemoryInfo.otherPss)} MB")
      )
    )

    return memoryInfo
  }

  fun calculateMemoryScore(): Double {
    subscribe()
    val totalMemoryMB = memoryInfo.totalMem / (1024 * 1024)
    val availableMemoryMB = memoryInfo.availMem / (1024 * 1024)

    val MIN_MEMORY_CAPABILITY = 2048.0 // 2GB
    val MAX_MEMORY_CAPABILITY = 16384.0 // 16GB

    val usedMemoryMB = totalMemoryMB - availableMemoryMB
    val memoryUsagePercentage = (usedMemoryMB / totalMemoryMB) * 100

    // Ajustar o score com base no uso de memória
    val normalizedMemory = ((totalMemoryMB - MIN_MEMORY_CAPABILITY) / (MAX_MEMORY_CAPABILITY - MIN_MEMORY_CAPABILITY)) * 100
    val adjustedScore = normalizedMemory - memoryUsagePercentage
    val score = adjustedScore.coerceIn(0.0, 100.0)

    return score
  }

  private fun getAppMemoryInfo(): android.os.Debug.MemoryInfo {
    val pid = android.os.Process.myPid()
    val memoryInfos = activityManager.getProcessMemoryInfo(intArrayOf(pid))
    return memoryInfos[0]
  }

  fun runCheckerMemory() {
    subscribe()

    val totalMemory = memoryInfo.totalMem
    val availableMemory = memoryInfo.availMem
    val threshold = memoryInfo.threshold
    val lowMemory = memoryInfo.lowMemory
    val usedMemory = totalMemory - availableMemory

    Log.i(
      "SystemMemoryManager",
      "Total Memory: ${formatBytesToMB(totalMemory)} MB"
    )
    Log.i(
      "SystemMemoryManager",
      "Available Memory: ${formatBytesToMB(availableMemory)} MB"
    )
    Log.i(
      "SystemMemoryManager",
      "Used Memory: ${formatBytesToMB(usedMemory)} MB"
    )
    Log.i(
      "SystemMemoryManager",
      "Threshold: ${formatBytesToMB(threshold)} MB"
    )
    Log.i(
      "SystemMemoryManager",
      "Low Memory: ${if (lowMemory) "Sim" else "Não"}"
    )
  }

  private fun formatBytesToMB(bytes: Long): String {
    val mb = bytes.toDouble() / (1024 * 1024)
    return "%.2f".format(mb)
  }

  private fun formatKBToMB(kb: Int): String {
    val mb = kb.toDouble() / 1024
    return "%.2f".format(mb)
  }
}