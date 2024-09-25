package com.padawanbr.systemmanager.managers

import android.app.Activity.ACTIVITY_SERVICE
import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import android.util.Log
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager


class MemoryManager(val context: Context) {

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

    val appMemoryInfo = getAppMemoryInfo() // Uso de memória do aplicativo

    val appMemoryLimit = getAppMemoryLimit(context) // Limite de memória do aplicativo

    val memoryUsagePercentage = checkAppMemoryUsage() // Uso de memória em porcentagem
    val isCloseToLimit = isMemoryUsageCloseToLimit() // Verifica se está próximo do limite

    val items = mutableListOf<Item>()

    // Informações gerais de memória do dispositivo
    items.add(Item("TOTAL_MEMORY", "${formatBytesToMB(totalMemory)} MB"))
    items.add(Item("AVAILABLE_MEMORY", "${formatBytesToMB(availableMemory)} MB"))
    items.add(Item("USED_MEMORY", "${formatBytesToMB(usedMemory)} MB"))
    items.add(Item("THRESHOLD", "${formatBytesToMB(threshold)} MB"))
    items.add(Item("LOW_MEMORY", if (lowMemory) "Sim" else "Não"))

    // Informações de memória do aplicativo
    items.add(Item("APP_MEMORY_LIMIT", "$appMemoryLimit MB")) // Limite de memória do aplicativo

    // Adicionar informações sobre o uso de memória em porcentagem
    items.add(Item("APP_MEMORY_USAGE_PERCENTAGE", "${"%.2f".format(memoryUsagePercentage)}%"))
    items.add(Item("APP_MEMORY_CLOSE_TO_LIMIT", if (isCloseToLimit) "Sim" else "Não"))

    // Uso de memória do aplicativo (PSS)
    items.add(Item("APP_TOTAL_PSS", "${formatKBToMB(appMemoryInfo.totalPss.toLong())} MB"))
    items.add(Item("APP_DALVIK_PSS", "${formatKBToMB(appMemoryInfo.dalvikPss.toLong())} MB"))
    items.add(Item("APP_NATIVE_PSS", "${formatKBToMB(appMemoryInfo.nativePss.toLong())} MB"))
    items.add(Item("APP_OTHER_PSS", "${formatKBToMB(appMemoryInfo.otherPss.toLong())} MB"))

    // Uso de memória do aplicativo (Private Dirty)
    items.add(Item("APP_TOTAL_PRIVATE_DIRTY", "${formatKBToMB(appMemoryInfo.totalPrivateDirty.toLong())} MB"))
    items.add(Item("APP_DALVIK_PRIVATE_DIRTY", "${formatKBToMB(appMemoryInfo.dalvikPrivateDirty.toLong())} MB"))
    items.add(Item("APP_NATIVE_PRIVATE_DIRTY", "${formatKBToMB(appMemoryInfo.nativePrivateDirty.toLong())} MB"))
    items.add(Item("APP_OTHER_PRIVATE_DIRTY", "${formatKBToMB(appMemoryInfo.otherPrivateDirty.toLong())} MB"))

    // Uso de memória do aplicativo (Shared Dirty)
    items.add(Item("APP_TOTAL_SHARED_DIRTY", "${formatKBToMB(appMemoryInfo.totalSharedDirty.toLong())} MB"))
    items.add(Item("APP_DALVIK_SHARED_DIRTY", "${formatKBToMB(appMemoryInfo.dalvikSharedDirty.toLong())} MB"))
    items.add(Item("APP_NATIVE_SHARED_DIRTY", "${formatKBToMB(appMemoryInfo.nativeSharedDirty.toLong())} MB"))
    items.add(Item("APP_OTHER_SHARED_DIRTY", "${formatKBToMB(appMemoryInfo.otherSharedDirty.toLong())} MB"))

    // Informações adicionais disponíveis a partir do API level 19 (KITKAT)
    items.add(Item("APP_TOTAL_PRIVATE_CLEAN", "${formatKBToMB(appMemoryInfo.totalPrivateClean.toLong())} MB"))
    items.add(Item("APP_TOTAL_SHARED_CLEAN", "${formatKBToMB(appMemoryInfo.totalSharedClean.toLong())} MB"))
    items.add(Item("APP_TOTAL_SWAPPABLE_PSS", "${formatKBToMB(appMemoryInfo.totalSwappablePss.toLong())} MB"))

    // Estatísticas de memória (a partir do API level 23)
    val memoryStats = appMemoryInfo.memoryStats
    for ((key, value) in memoryStats) {
      items.add(Item("APP_MEMORY_STAT_$key", "$value"))
    }

    val memoryInfoManager = Manager(
      title = "Informações de Memória",
      items = items
    )

    return memoryInfoManager
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
    val normalizedMemory =
      ((totalMemoryMB - MIN_MEMORY_CAPABILITY) / (MAX_MEMORY_CAPABILITY - MIN_MEMORY_CAPABILITY)) * 100
    val adjustedScore = normalizedMemory - memoryUsagePercentage
    val score = adjustedScore.coerceIn(0.0, 100.0)

    return score
  }

  private fun getAppMemoryInfo(): Debug.MemoryInfo {
    val pid = android.os.Process.myPid()
    val memoryInfos = activityManager.getProcessMemoryInfo(intArrayOf(pid))
    return memoryInfos[0]
  }

  fun getAppMemoryUsage(): Debug.MemoryInfo {
    val memoryInfo = Debug.MemoryInfo()
    Debug.getMemoryInfo(memoryInfo)
    return memoryInfo
  }

  fun getAppMemoryLimit(context: Context): Int {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val memoryClass = activityManager.memoryClass // em MB
    return memoryClass
  }

  fun isMemoryUsageCloseToLimit(): Boolean {
    val memoryInfo = getAppMemoryUsage()
    val totalPssKB = memoryInfo.totalPss.toDouble()
    val totalPssMB = totalPssKB / 1024.0 // Converter para MB

    val memoryLimitMB = getAppMemoryLimit(context).toDouble()

    // Defina um threshold, por exemplo, 80% do limite de memória
    val threshold = memoryLimitMB * 0.8

    return totalPssMB >= threshold
  }

  fun checkAppMemoryUsage(): Double {
    val memoryInfo = getAppMemoryUsage()
    val totalPssKB = memoryInfo.totalPss.toDouble() //Total de memória PSS utilizada pelo aplicativo em kilobytes.
    val totalPssMB = totalPssKB / 1024.0 //Converte o valor para megabytes dividindo por 1024.

    val memoryLimitMB = getAppMemoryLimit(context).toDouble() //Retorna o limite de memória imposto pelo sistema ao aplicativo, em megabytes.

    val usagePercentage = (totalPssMB / memoryLimitMB) * 100 //Calcula a porcentagem de memória usada pelo aplicativo em relação ao limite.

    Log.i(
      "AppMemoryUsage",
      "O aplicativo está usando $totalPssMB MB de $memoryLimitMB MB (${usagePercentage.toInt()}% do limite)"
    )

    if (usagePercentage >= 80.0) {
      Log.w("AppMemoryUsage", "Aviso: O aplicativo está usando mais de 80% do limite de memória")
    } else {
      Log.w("AppMemoryUsage", "Aviso: Uso de memória está dentro dos limites aceitáveis")
    }

    return usagePercentage
  }

  private fun formatBytesToMB(bytes: Long): String {
    val mb = bytes.toDouble() / (1024 * 1024)
    return "%.2f".format(mb)
  }

  private fun formatKBToMB(kb: Int): String {
    val mb = kb.toDouble() / 1024
    return "%.2f".format(mb)
  }

  private fun formatKBToMB(kb: Double): String {
    val mb = kb / 1024
    return "%.2f".format(mb)
  }

  private fun formatKBToMB(kb: Long): String {
    val mb = kb.toDouble() / 1024
    return "%.2f".format(mb)
  }

}