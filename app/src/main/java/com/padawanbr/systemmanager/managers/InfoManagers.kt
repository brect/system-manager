package com.padawanbr.systemmanager.managers

import android.content.Context

class InfoManagers(context: Context) {

  val deviceInfoManager = DeviceInfoManager(context)
  val memoryManager = MemoryManager(context)
  val deviceBateryManager = DeviceBateryManager(context)
  val gpuInfoManager = GpuInfoManager()
  val storageInfoManager = StorageInfoManager()
  val developerOptionsInfoManager = DeveloperOptionsManager(context)

  val managers = listOf(
    deviceInfoManager.deviceInfo(),
    deviceInfoManager.screenInfo(),
    memoryManager.mobileMemoryInfo(),
    memoryManager.appMemoryInfo(),
    memoryManager.appMemoryOtherInfo(),
    deviceBateryManager.bateryInfo(),
    gpuInfoManager.gpuInfo(),
    storageInfoManager.getInternalStorageInfo(),
    developerOptionsInfoManager.developerOptionsInfo(),
//    networkInfoManager.getNetworkInfo())
  )
}