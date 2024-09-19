package com.padawanbr.systemmanager.managers

import android.content.Context

class InfoManagers(val context: Context) {

  val deviceInfoManager = DeviceInfoManager()
  val memoryManager  = MemoryManager(context)
  val deviceBateryManager = DeviceBateryManager(context)
  val gpuInfoManager = GpuInfoManager()
  val storageInfoManager = StorageInfoManager()

  val managers = listOf(
    deviceInfoManager.deviceInfo(),
    memoryManager.memoryInfo()
//    memoryManager.subscribe()
//    memoryManager.runCheckerMemory(),
//    deviceInfoManager.deviceInfo(),
//    deviceInfoManager.screenInfo(),
//    deviceBateryManager.logBateryInfo(),
//    gpuInfoManager.gpuInfo(),
//    storageInfoManager.getStorageInfo(),
//
//    networkInfoManager.getNetworkInfo())
  )
}