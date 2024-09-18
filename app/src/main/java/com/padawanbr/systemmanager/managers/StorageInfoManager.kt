package com.padawanbr.systemmanager.managers

import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class StorageInfoManager {

  fun getStorageInfo() {
    getInternalStorageInfo()
//    getExternalStorageInfo()
//    getStorageHardwareInfo()
  }

  fun getInternalStorageInfo(): String {
    val internalStorageType = getInternalStorageType()
    val internalStorageTotal = getTotalInternalStorageSize()
    val internalStorageAvailable = getAvailableInternalStorageSize()

    val internalInfo = "Armazenamento Interno:\n" +
        "Sistema de Arquivos: $internalStorageType\n" +
        "Capacidade Total: ${formatSize(internalStorageTotal)}\n" +
        "Espaço Disponível: ${formatSize(internalStorageAvailable)}\n"

    Log.i("StorageInfoManager", internalInfo)
    return internalInfo
  }

  fun getExternalStorageInfo(): String {
    val externalStorageType = getExternalStorageType()
    val externalStorageTotal = getTotalExternalStorageSize()
    val externalStorageAvailable = getAvailableExternalStorageSize()
    val externalStorageStatus = getExternalStorageState()

    val externalInfo = "Armazenamento Externo:\n" +
        "Sistema de Arquivos: $externalStorageType\n" +
        "Status: $externalStorageStatus\n" +
        "Capacidade Total: ${formatSize(externalStorageTotal)}\n" +
        "Espaço Disponível: ${formatSize(externalStorageAvailable)}\n"

    Log.i("StorageInfoManager", externalInfo)
    return externalInfo
  }

  private fun getStorageHardwareInfo(): String {
    try {
      val storageInfo = System.getProperty("ro.boot.bootdevice") ?: "Desconhecido"
      Log.i("StorageInfoManager", "Hardware de Armazenamento: $storageInfo")
      return storageInfo
    } catch (e: Exception) {
      e.printStackTrace()
      return "Erro ao obter informações do hardware de armazenamento"
    }
  }

  private fun getInternalStorageType(): String {
    try {
      val mountsFile = File("/proc/mounts")
      if (mountsFile.exists()) {
        BufferedReader(FileReader(mountsFile)).use { reader ->
          var line: String?
          while (reader.readLine().also { line = it } != null) {
            if (line!!.contains("/data")) {
              val tokens = line!!.split(" ")
              if (tokens.size >= 3) {
                val filesystemType = tokens[2]
                return filesystemType
              }
            }
          }
        }
      }
      return "Desconhecido"
    } catch (e: Exception) {
      e.printStackTrace()
      return "Erro ao obter o tipo de armazenamento"
    }
  }

  private fun getTotalInternalStorageSize(): Long {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      stat.blockSizeLong * stat.blockCountLong
    } else {
      stat.blockSize.toLong() * stat.blockCount.toLong()
    }
  }

  private fun getAvailableInternalStorageSize(): Long {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      stat.blockSizeLong * stat.availableBlocksLong
    } else {
      stat.blockSize.toLong() * stat.availableBlocks.toLong()
    }
  }

  private fun getExternalStorageType(): String {
    try {
      val mountsFile = File("/proc/mounts")
      if (mountsFile.exists()) {
        BufferedReader(FileReader(mountsFile)).use { reader ->
          var line: String?
          val externalPath = Environment.getExternalStorageDirectory().absolutePath
          while (reader.readLine().also { line = it } != null) {
            if (line!!.contains(" $externalPath ")) {
              val tokens = line!!.split(" ")
              if (tokens.size >= 3) {
                val filesystemType = tokens[2]
                return filesystemType
              }
            }
          }
        }
      }
      return "Desconhecido"
    } catch (e: Exception) {
      e.printStackTrace()
      return "Erro ao obter o tipo de armazenamento"
    }
  }

  private fun getExternalStorageState(): String {
    return Environment.getExternalStorageState()
  }

  private fun isExternalStorageRemovable(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      Environment.isExternalStorageRemovable()
    } else {
      // Antes do API level 9, assume-se que é removível
      true
    }
  }

  private fun getTotalExternalStorageSize(): Long {
    if (!isExternalStorageAvailable()) {
      return 0L
    }
    val path = Environment.getExternalStorageDirectory()
    val stat = StatFs(path.path)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      stat.blockSizeLong * stat.blockCountLong
    } else {
      stat.blockSize.toLong() * stat.blockCount.toLong()
    }
  }

  private fun getAvailableExternalStorageSize(): Long {
    if (!isExternalStorageAvailable()) {
      return 0L
    }
    val path = Environment.getExternalStorageDirectory()
    val stat = StatFs(path.path)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      stat.blockSizeLong * stat.availableBlocksLong
    } else {
      stat.blockSize.toLong() * stat.availableBlocks.toLong()
    }
  }

  private fun isExternalStorageAvailable(): Boolean {
    return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED ||
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY
  }

  private fun formatSize(size: Long): String {
    var sizeInBytes = size.toDouble()
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var unitIndex = 0
    while (sizeInBytes >= 1024 && unitIndex < units.size - 1) {
      sizeInBytes /= 1024
      unitIndex++
    }
    return String.format("%.2f %s", sizeInBytes, units[unitIndex])
  }
}
