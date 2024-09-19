package com.padawanbr.systemmanager.managers

import android.content.Context

class InfoManagers(context: Context) {

  val deviceInfoManager = DeviceInfoManager(context)
  val memoryManager = MemoryManager(context)
  val deviceBateryManager = DeviceBateryManager(context)
  val gpuInfoManager = GpuInfoManager()
  val storageInfoManager = StorageInfoManager()

  val managers = listOf(
    deviceInfoManager.deviceInfo(),
    deviceInfoManager.screenInfo(),
    memoryManager.memoryInfo(),
    deviceBateryManager.bateryInfo(),
    gpuInfoManager.gpuInfo(),
    storageInfoManager.getInternalStorageInfo(),
//    networkInfoManager.getNetworkInfo())
  )
}