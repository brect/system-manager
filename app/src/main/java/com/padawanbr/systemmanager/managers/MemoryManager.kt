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

    val memoryInfo = Manager(
      title = "Memory Info",
      items = listOf(
        Item("TOTAL_MEMORY", "${memoryInfo.totalMem} bytes"),
        Item("AVAILABLE_MEMORY", "${memoryInfo.availMem} bytes"),
        Item("TRESHOLD", "${memoryInfo.threshold} bytes"),
        Item("LOW_MEMORY", "${memoryInfo.lowMemory}"),
      )
    )

    return memoryInfo
  }

  fun runCheckerMemory() {
    val totalMemory = memoryInfo.totalMem // Memória RAM total do dispositivo
    val availableMemory = memoryInfo.availMem // Memória RAM disponível
    val threshold = memoryInfo.threshold // Limite de uso de memória
    val lowMemory = memoryInfo.lowMemory // Indica se a memória RAM está baixa
    val usedMemory = totalMemory - availableMemory // Memória RAM utilizada

    Log.i(
      "SystemMemoryManager",
      " memoryInfo.totalMemory $totalMemory bytes | ${bytesToMegabytes(totalMemory)} megabytes"
    )
    Log.i(
      "SystemMemoryManager",
      " memoryInfo.availableMemory $availableMemory bytes | ${bytesToMegabytes(availableMemory)} megabytes"
    )
    Log.i(
      "SystemMemoryManager",
      " memoryInfo.threshold $threshold bytes | ${bytesToMegabytes(threshold)} megabytes"
    )
    Log.i("SystemMemoryManager", " memoryInfo.lowMemory $lowMemory")
    Log.i(
      "SystemMemoryManager",
      " memoryInfo.usedMemory $usedMemory bytes | ${bytesToMegabytes(usedMemory)} megabytes"
    )

  }

  fun bytesToMegabytes(bytes: Long): Int {
    return (bytes / (1024L * 1024L)).toInt()
  }
}