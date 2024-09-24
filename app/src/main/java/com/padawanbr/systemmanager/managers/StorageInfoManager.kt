package com.padawanbr.systemmanager.managers

import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class StorageInfoManager {

  fun getInternalStorageInfo(): Manager {
    val internalStorageType = getInternalStorageType()
    val internalStorageTotal = getTotalInternalStorageSize()
    val internalStorageAvailable = getAvailableInternalStorageSize()

    val internalInfo = Manager(
      title = "Storage Info",
      items = listOf(
        Item(key = "STORAGE", value = "Internal Storage"),
        Item(key = "INTERNAL STORAGE TYPE", value = internalStorageType),
        Item(key = "INTERNAL STORAGE TOTAL", value = formatSize(internalStorageTotal)),
        Item(key = "INTERNAL STORAGE AVAILABLE", value = formatSize(internalStorageAvailable)),
      )
    )

    return internalInfo
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

  fun calculateStorageScore(): Double {
    val totalInternalStorageGB = getTotalInternalStorageSize() / (1024.0 * 1024 * 1024)
    val MAX_STORAGE_CAPABILITY = 256.0 // Exemplo: 256GB

    val score = (totalInternalStorageGB / MAX_STORAGE_CAPABILITY) * 100
    return score.coerceIn(0.0, 100.0)
  }
}
