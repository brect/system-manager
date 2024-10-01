package com.padawanbr.systemmanager.managers

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager

class DeviceBateryManager(val context: Context) {

  fun bateryInfo(): Manager {
    val batteryInfo = Manager(
      title = "Informações da Bateria",
      items = listOf(
        Item(key = "BATTERY_HEALTH", value = getBatteryHealth()),
        Item(key = "BATTERY_LEVEL", value = "${getBatteryLevel()}%"),
        Item(key = "BATTERY_STATUS", value = getBatteryStatus()),
        Item(key = "POWER_SOURCE", value = getPowerSource()),
        Item(key = "BATTERY_TECHNOLOGY", value = getBatteryTechnology()),
        Item(key = "BATTERY_TEMPERATURE", value = "${getBatteryTemperature()}°C"),
        Item(key = "BATTERY_TEMPERATURE_STATUS", value = getBatteryTemperatureStatus()),
        Item(key = "BATTERY_TEMPERATURE_STATUS_PERCENT", value = "${getBatteryTemperatureScore()}%"),
        Item(key = "BATTERY_VOLTAGE", value = "${getBatteryVoltage()} mV"),
        Item(key = "BATTERY_CAPACITY", value = "${getBatteryCapacity()} mAh"),
        Item(key = "BATTERY_SCORE", value = "${calculateBatteryScore()} mAh"),
      )
    )

    checkBatteryTemperature()

    return batteryInfo
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

  private fun getBatteryHealthScore(): Double {
    return when (getBatteryHealth()) {
      "Boa" -> 100.0
      "Fria" -> 90.0
      "Superaquecimento" -> 50.0
      "Descarregada", "Falha", "Sobretensão" -> 30.0
      else -> 70.0
    }
  }

  fun getBatteryTemperatureStatus(): String {
    val temperature = getBatteryTemperature() // Em graus Celsius
    return when {
      temperature < 30 -> "Ótima"
      temperature < 35 -> "Boa"
      temperature < 40 -> "Elevada"
      else -> "Crítica"
    }
  }

  private fun getBatteryTemperatureScore(): Double {
    val temperature = getBatteryTemperature()
    return when {
      temperature < 30 -> 100.0
      temperature < 35 -> 80.0
      temperature < 40 -> 50.0
      else -> 20.0
    }
  }

  fun checkBatteryTemperature() {
    val temperatureStatus = getBatteryTemperatureStatus()
    if (temperatureStatus == "Elevada" || temperatureStatus == "Crítica") {
      // Notificar o usuário
      Log.i(
        "BATTERY_TEMPERATURE_STATUS",
        "A temperatura do dispositivo está $temperatureStatus. Considere fechar alguns aplicativos."
      )
    }
  }

  fun calculateBatteryScore(): Double {
    val batteryLevel = getBatteryLevel() // Em porcentagem
    val batteryHealthScore = getBatteryHealthScore()
    val score = (batteryLevel * 0.7) + (batteryHealthScore * 0.3)
    return score.coerceIn(0.0, 100.0)
  }

}