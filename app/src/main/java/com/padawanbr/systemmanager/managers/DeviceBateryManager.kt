package com.padawanbr.systemmanager.managers

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log

class DeviceBateryManager(val context: Context) {

  fun logBateryInfo() {
    Log.i("DeviceBateryManager", "getBatteryHealth: ${getBatteryHealth()}\n")
    Log.i("DeviceBateryManager", "getBatteryLevel: ${getBatteryLevel()}\n")
    Log.i("DeviceBateryManager", "getBatteryStatus: ${getBatteryStatus()}\n")
    Log.i("DeviceBateryManager", "getPowerSource: ${getPowerSource()}\n")
    Log.i("DeviceBateryManager", "getBatteryTechnology: ${getBatteryTechnology()}\n")
    Log.i("DeviceBateryManager", "getBatteryTemperature: ${getBatteryTemperature()}\n")
    Log.i("DeviceBateryManager", "getBatteryVoltage: ${getBatteryVoltage()}\n")
    Log.i("DeviceBateryManager", "getBatteryCapacity: ${getBatteryCapacity()}\n")
  }

  fun getBatteryHealth(): String {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val health = intent?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1) ?: -1
    return when (health) {
      BatteryManager.BATTERY_HEALTH_GOOD -> "Boa"
      BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Superaquecimento"
      BatteryManager.BATTERY_HEALTH_DEAD -> "Descarregada"
      BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Sobretensão"
      BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Falha"
      BatteryManager.BATTERY_HEALTH_COLD -> "Fria"
      else -> "Desconhecida"
    }
  }

  fun getBatteryLevel(): Int {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level != -1 && scale != -1) {
      (level * 100) / scale
    } else {
      -1
    }
  }

  fun getBatteryStatus(): String {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
    return when (status) {
      BatteryManager.BATTERY_STATUS_CHARGING -> "Carregando"
      BatteryManager.BATTERY_STATUS_DISCHARGING -> "Descarregando"
      BatteryManager.BATTERY_STATUS_FULL -> "Cheia"
      BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "Não está carregando"
      else -> "Desconhecido"
    }
  }

  fun getPowerSource(): String {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val plugged = intent?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
    return when (plugged) {
      BatteryManager.BATTERY_PLUGGED_AC -> "Tomada AC"
      BatteryManager.BATTERY_PLUGGED_USB -> "USB"
      BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Carregamento sem fio"
      else -> "Bateria"
    }
  }

  fun getBatteryTechnology(): String {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    return intent?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Desconhecido"
  }

  fun getBatteryTemperature(): Float {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val temp = intent?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) ?: -1
    return temp / 10f
  }

  fun getBatteryVoltage(): Int {
    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    return intent?.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1) ?: -1
  }

  fun getBatteryCapacity(): Double {
    // A obtenção da capacidade da bateria pode não funcionar em todos os dispositivos
    val powerProfileClass = "com.android.internal.os.PowerProfile"
    return try {
      val mPowerProfile = Class.forName(powerProfileClass)
        .getConstructor(Context::class.java)
        .newInstance(context)
      val batteryCapacity = Class.forName(powerProfileClass)
        .getMethod("getBatteryCapacity")
        .invoke(mPowerProfile) as Double
      batteryCapacity
    } catch (e: Exception) {
      e.printStackTrace()
      0.0
    }
  }
}