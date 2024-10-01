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

  init {
    activityManager.getMemoryInfo(memoryInfo)
  }

  //Fornece informações sobre a memória total, disponível e usada do dispositivo.
  fun mobileMemoryInfo(): Manager {

//    activityManager.getMemoryInfo(memoryInfo)

    val totalMemory = memoryInfo.totalMem // Memória RAM total do dispositivo
    val availableMemory = memoryInfo.availMem // Memória RAM disponível
    val usedMemory = totalMemory - availableMemory // Memória RAM utilizada
    val threshold = memoryInfo.threshold // Limite de uso de memória
    val lowMemory = memoryInfo.lowMemory // Indica se a memória RAM está baixa

    val items = mutableListOf<Item>()

    // Informações gerais de memória do dispositivo
    items.add(Item("TOTAL_MEMORY", "${formatBytesToMB(totalMemory)} MB"))
    items.add(Item("AVAILABLE_MEMORY", "${formatBytesToMB(availableMemory)} MB"))
    items.add(Item("USED_MEMORY", "${formatBytesToMB(usedMemory)} MB"))
    items.add(Item("THRESHOLD", "${formatBytesToMB(threshold)} MB"))
    items.add(Item("LOW_MEMORY", if (lowMemory) "Sim" else "Não"))

    val memoryInfoManager = Manager(
      title = "Info Device Memory",
      items = items
    )

    return memoryInfoManager
  }

  //Fornece informações sobre a memória alocada para o aplicativo, incluindo limites e porcentagem de uso.
  fun appMemoryInfo(): Manager {

    val appMemoryInfo = getAppMemoryUsage() // Uso de memória do aplicativo

    val items = mutableListOf<Item>()

    items.add(Item("MAX_MEMORY", "${formatBytesToMB(getMaximumMemory())} MB"))
    items.add(Item("MEMORY_CLASS_LIMIT", "${getMemoryClass()} MB"))
    items.add(Item("LARGE_MEMORY_CLASS_LIMIT", "${getLargeMemoryClass()} MB"))

    // Adicionar informações sobre o uso de memória em porcentagem
    items.add(Item("APP_MEMORY_USAGE_PERCENTAGE", "${"%.2f".format(checkAppMemoryUsage())}%"))
    items.add(Item("APP_MEMORY_CLOSE_TO_LIMIT", if (isMemoryUsageCloseToLimit()) "Sim" else "Não"))

    // Uso de memória do aplicativo (PSS)
    items.add(Item("APP_TOTAL_PSS", "${formatKBToMB(appMemoryInfo.totalPss.toLong())} MB"))
    items.add(Item("APP_DALVIK_PSS", "${formatKBToMB(appMemoryInfo.dalvikPss.toLong())} MB"))
    items.add(Item("APP_NATIVE_PSS", "${formatKBToMB(appMemoryInfo.nativePss.toLong())} MB"))
    items.add(Item("APP_OTHER_PSS", "${formatKBToMB(appMemoryInfo.otherPss.toLong())} MB"))

    val memoryInfoManager = Manager(
      title = "Info APP Memory",
      items = items
    )

    return memoryInfoManager
  }

  //Fornece informações detalhadas sobre o uso de memória do aplicativo, como PSS, memória privada e compartilhada.
  fun appMemoryOtherInfo(): Manager {

    val items = mutableListOf<Item>()

    val appMemoryInfo = getAppMemoryUsage() // Uso de memória do aplicativo

    // Uso de memória do aplicativo (Private Dirty)
    items.add(
      Item(
        "APP_TOTAL_PRIVATE_DIRTY",
        "${formatKBToMB(appMemoryInfo.totalPrivateDirty.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_DALVIK_PRIVATE_DIRTY",
        "${formatKBToMB(appMemoryInfo.dalvikPrivateDirty.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_NATIVE_PRIVATE_DIRTY",
        "${formatKBToMB(appMemoryInfo.nativePrivateDirty.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_OTHER_PRIVATE_DIRTY",
        "${formatKBToMB(appMemoryInfo.otherPrivateDirty.toLong())} MB"
      )
    )

    // Uso de memória do aplicativo (Shared Dirty)
    items.add(
      Item(
        "APP_TOTAL_SHARED_DIRTY",
        "${formatKBToMB(appMemoryInfo.totalSharedDirty.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_DALVIK_SHARED_DIRTY",
        "${formatKBToMB(appMemoryInfo.dalvikSharedDirty.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_NATIVE_SHARED_DIRTY",
        "${formatKBToMB(appMemoryInfo.nativeSharedDirty.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_OTHER_SHARED_DIRTY",
        "${formatKBToMB(appMemoryInfo.otherSharedDirty.toLong())} MB"
      )
    )

    // Informações adicionais disponíveis a partir do API level 19 (KITKAT)
    items.add(
      Item(
        "APP_TOTAL_PRIVATE_CLEAN",
        "${formatKBToMB(appMemoryInfo.totalPrivateClean.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_TOTAL_SHARED_CLEAN",
        "${formatKBToMB(appMemoryInfo.totalSharedClean.toLong())} MB"
      )
    )
    items.add(
      Item(
        "APP_TOTAL_SWAPPABLE_PSS",
        "${formatKBToMB(appMemoryInfo.totalSwappablePss.toLong())} MB"
      )
    )

    // Estatísticas de memória (a partir do API level 23)
    val memoryStats = appMemoryInfo.memoryStats
    for ((key, value) in memoryStats) {
      items.add(Item("APP_MEMORY_STAT_$key", "$value"))
    }

    val memoryInfoManager = Manager(
      title = "Info Other App Memory",
      items = items
    )

    return memoryInfoManager
  }

  //obtêm informações de memória do aplicativo.
  private fun getAppMemoryInfo(): Debug.MemoryInfo {
    val pid = android.os.Process.myPid()
    val memoryInfos = activityManager.getProcessMemoryInfo(intArrayOf(pid))
    return memoryInfos[0]
  }

  //obtêm informações de memória do aplicativo.
  fun getAppMemoryUsage(): Debug.MemoryInfo {
    val memoryInfo = Debug.MemoryInfo()
    Debug.getMemoryInfo(memoryInfo)
    return memoryInfo
  }

  // Retorna o máximo de memória que o aplicativo pode usar, conforme definido pela JVM.
  fun getMaximumMemory(): Long {
    val rt = Runtime.getRuntime()
    return rt.maxMemory()
  }

  // Retorna o limite de heap em megabytes definido pelo sistema para o aplicativo.
  fun getMemoryClass(): Int {
    return activityManager.memoryClass
  }

  // Retorna o limite de memória para aplicativos que solicitam o modo de memória grande.
  fun getLargeMemoryClass(): Int {
    return activityManager.largeMemoryClass
  }

  // Uso de memória em porcentagem
  fun checkAppMemoryUsage(): Double {
    val memoryInfo = getAppMemoryUsage()

    //Total de memória PSS utilizada pelo aplicativo em kilobytes.
    val totalPssKB = memoryInfo.totalPss.toDouble()

    //Converte o valor para megabytes dividindo por 1024.
    val totalPssMB = totalPssKB / 1024.0

    //Retorna o limite de memória imposto pelo sistema ao aplicativo, em megabytes.
    val memoryLimitMB = getMemoryClass().toDouble()

    //Calcula a porcentagem de memória usada pelo aplicativo em relação ao limite.
    val usagePercentage = (totalPssMB / memoryLimitMB) * 100

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

  // Verifica se está próximo do limite
  fun isMemoryUsageCloseToLimit(): Boolean {
    val memoryInfo = getAppMemoryUsage()
    val totalPssKB = memoryInfo.totalPss.toDouble()
    val totalPssMB = totalPssKB / 1024.0 // Converter para MB

    val memoryLimitMB = getMemoryClass().toDouble()

    // Defina um threshold, por exemplo, 70% do limite de memória
    val threshold = memoryLimitMB * 0.70

    return totalPssMB >= threshold
  }

  private fun formatKBToMB(kb: Number): String {
    val mb = kb.toDouble() / 1024
    return "%.2f".format(mb)
  }


  private fun formatBytesToMB(bytes: Long): String {
    val mb = bytes.toDouble() / (1024 * 1024)
    return "%.2f".format(mb)
  }

  private fun formatKBToMB(kb: Int): String {
    val mb = kb.toDouble() / 1024.0
    return "%.2f".format(mb)
  }

}