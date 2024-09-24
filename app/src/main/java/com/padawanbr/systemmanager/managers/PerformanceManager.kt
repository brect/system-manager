package com.padawanbr.systemmanager.managers

import android.content.Context
import android.util.Log


class PerformanceManager(val context: Context) {

  private val deviceInfoManager = DeviceInfoManager(context)
  private val memoryManager = MemoryManager(context)
  private val gpuInfoManager = GpuInfoManager()
  private val developerOptionsManager = DeveloperOptionsManager(context)
  private val deviceBatteryManager = DeviceBateryManager(context)
  private val storageInfoManager = StorageInfoManager()
  private val networkInfoManager = NetworkInfoManager(context)

  fun calculateOverallScore(): Double {
    val cpuScore = deviceInfoManager.calculateCpuScore()
    val memoryScore = memoryManager.calculateMemoryScore()
    val gpuScore = gpuInfoManager.calculateGpuScore()
    val batteryScore = deviceBatteryManager.calculateBatteryScore()
    val storageScore = storageInfoManager.calculateStorageScore()
    val developerImpact = developerOptionsManager.calculateDeveloperOptionsImpact()
    val networkScore = networkInfoManager.calculateNetworkScore()

    // Pesos para cada componente (ajuste conforme necessário)
    val overallScore = (
        (cpuScore * 0.25) +
            (memoryScore * 0.20) +
            (gpuScore * 0.20) +
            (batteryScore * 0.15) +
            (storageScore * 0.10) +
            (networkScore * 0.10) +
            developerImpact
        ).coerceIn(0.0, 100.0)

    return overallScore
  }

  fun evaluatePerformance() {
    val overallScore = calculateOverallScore()
    val THRESHOLD_LIGHT_MODE = 60.0

    if (overallScore < THRESHOLD_LIGHT_MODE) {
      enableLightMode()
    } else {
      disableLightMode()
    }
  }

  private fun enableLightMode() {
    // Implementar ajustes para reduzir o uso de recursos
    Log.i("PerformanceManager", "Modo Light Ativado")
  }

  private fun disableLightMode() {
    // Reverter ajustes caso o dispositivo melhore o desempenho
    Log.i("PerformanceManager", "Modo Light Desativado")
  }
}