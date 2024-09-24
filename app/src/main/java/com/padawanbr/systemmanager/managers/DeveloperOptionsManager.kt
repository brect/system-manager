package com.padawanbr.systemmanager.managers

import android.content.Context
import android.provider.Settings
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class DeveloperOptionsManager(private val context: Context) {

  fun developerOptionsInfo(): Manager {
    val developerOptions = Manager(
      title = "Developer Options",
      items = listOf(
        Item(key = "DEVELOPMENT_SETTINGS", value = isDeveloperOptionsEnabled().toString()),
        Item(key = "DONT_KEEP_ACTIVITIES", value = isDontKeepActivitiesEnabled().toString()),
        Item(key = "USB_DEBUGGING", value = isUsbDebuggingEnabled().toString()),
        Item(key = "DEVICE_ROOTED", value = isDeviceRooted().toString()),
      )
    )

    return developerOptions
  }

  fun isDeveloperOptionsEnabled(): Boolean {
    return try {
      val devModeEnabled =
        Settings.Global.getInt(
          context.contentResolver,
          Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
          0
        )
      devModeEnabled == 1
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  // Verifica se a opção "Não manter atividades" está ativa
  fun isDontKeepActivitiesEnabled(): Boolean {
    return try {
      Settings.Global.getInt(
        context.contentResolver,
        Settings.Global.ALWAYS_FINISH_ACTIVITIES,
        0
      ) == 1
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  // Verifica se a depuração USB está ativa
  fun isUsbDebuggingEnabled(): Boolean {
    return try {
      val adbEnabled =
        Settings.Global.getInt(
          context.contentResolver,
          Settings.Global.ADB_ENABLED,
          0
        )
      adbEnabled == 1
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  // Verifica se o dispositivo está com acesso root ativo
  fun isDeviceRooted(): Boolean {
    return checkBuildTags() || checkSuperUserApk() || checkSuExists()
  }

  private fun checkBuildTags(): Boolean {
    val buildTags = android.os.Build.TAGS
    return buildTags != null && buildTags.contains("test-keys")
  }

  private fun checkSuperUserApk(): Boolean {
    val paths = arrayOf(
      "/system/app/Superuser.apk",
      "/system/app/SuperSU.apk",
      "/system/app/Magisk.apk",
      "/system/app/Kinguser.apk",
      "/system/app/KingRoot.apk"
    )
    return paths.any { File(it).exists() }
  }

  private fun checkSuExists(): Boolean {
    return try {
      val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
      val reader = BufferedReader(InputStreamReader(process.inputStream))
      reader.readLine() != null
    } catch (e: Exception) {
      false
    }
  }

  fun calculateDeveloperOptionsImpact(): Double {
    var impact = 0.0
    if (isDontKeepActivitiesEnabled()) {
      impact -= 20.0
    }
    if (isUsbDebuggingEnabled()) {
      impact -= 10.0
    }
    if (isDeviceRooted()) {
      impact -= 5.0
    }
    return impact
  }
}