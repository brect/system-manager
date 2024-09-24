package com.padawanbr.systemmanager.managers

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.GLES20
import android.util.Log
import com.padawanbr.systemmanager.model.Item
import com.padawanbr.systemmanager.model.Manager
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class GpuInfoManager {

  // Variáveis para o contexto EGL
  private var eglDisplay = EGL14.EGL_NO_DISPLAY
  private var eglContext = EGL14.EGL_NO_CONTEXT
  private var eglSurface = EGL14.EGL_NO_SURFACE

  fun gpuInfo(): Manager {
    val gpuInfo = Manager(
      title = "GPU Info",
      items = listOf(
        Item(key = "GPU Vendor", value = getGpuVendor()),
        Item(key = "GPU Renderer", value = getGpuRenderer()),
        Item(key = "GPU Load", value = getGpuLoad()),
        Item(key = "ScalingGovernor", value = getScalingGovernor()),
      )
    )
    return gpuInfo
  }

  fun calculateGpuScore(): Double {
    val gpuRenderer = getGpuRenderer()
    val gpuScores = mapOf(
      "Adreno 730" to 100,
      "Adreno 660" to 90,
      "Mali-G78" to 85,
      "Mali-G77" to 80,
      "PowerVR GM9446" to 75,
      "Unknown" to 50
    )
    for ((key, value) in gpuScores) {
      if (gpuRenderer.contains(key)) {
        return value.toDouble()
      }
    }
    return 50.0 // Score padrão para GPUs desconhecidas
  }

  fun getGpuVendor(): String {
    if (initializeOpenGL()) {
      val vendor = GLES20.glGetString(GLES20.GL_VENDOR) ?: "Desconhecido"
      cleanupOpenGL()
      return vendor
    } else {
      return "Não foi possível inicializar OpenGL"
    }
  }

  fun getGpuRenderer(): String {
    if (initializeOpenGL()) {
      val renderer = GLES20.glGetString(GLES20.GL_RENDERER) ?: "Desconhecido"
      cleanupOpenGL()
      return renderer
    } else {
      return "Não foi possível inicializar OpenGL"
    }
  }

  fun getScalingGovernor(): String {
    return try {
      val governorFile = File("/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
      if (governorFile.exists()) {
        BufferedReader(FileReader(governorFile)).use { it.readLine() }
      } else {
        "Não disponível"
      }
    } catch (e: Exception) {
      e.printStackTrace()
      "Erro ao obter"
    }
  }

  fun getGpuLoad(): String {
    // Não é possível obter a carga da GPU sem acesso root ou APIs específicas do fabricante
    return "Não disponível"
  }

  private fun initializeOpenGL(): Boolean {
    try {
      eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
      if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
        Log.e("GpuInfoManager", "Não foi possível obter o EGL Display")
        return false
      }

      val version = IntArray(2)
      if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
        Log.e("GpuInfoManager", "Não foi possível inicializar o EGL")
        return false
      }

      val attribList = intArrayOf(
        EGL14.EGL_RED_SIZE, 8,
        EGL14.EGL_GREEN_SIZE, 8,
        EGL14.EGL_BLUE_SIZE, 8,
        EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
        EGL14.EGL_NONE
      )

      val configs = arrayOfNulls<EGLConfig>(1)
      val numConfigs = IntArray(1)
      if (!EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, 1, numConfigs, 0)) {
        Log.e("GpuInfoManager", "Não foi possível escolher a configuração EGL")
        return false
      }
      val eglConfig = configs[0]

      val attribListContext = intArrayOf(
        EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
        EGL14.EGL_NONE
      )
      eglContext =
        EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, attribListContext, 0)
      if (eglContext == EGL14.EGL_NO_CONTEXT) {
        Log.e("GpuInfoManager", "Não foi possível criar o contexto EGL")
        return false
      }

      val surfaceAttribs = intArrayOf(
        EGL14.EGL_WIDTH, 1,
        EGL14.EGL_HEIGHT, 1,
        EGL14.EGL_NONE
      )
      eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, eglConfig, surfaceAttribs, 0)
      if (eglSurface == EGL14.EGL_NO_SURFACE) {
        Log.e("GpuInfoManager", "Não foi possível criar o surface EGL")
        return false
      }

      if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
        Log.e("GpuInfoManager", "Não foi possível tornar o contexto EGL atual")
        return false
      }

      return true
    } catch (e: Exception) {
      e.printStackTrace()
      return false
    }
  }

  private fun cleanupOpenGL() {
    try {
      if (eglDisplay != EGL14.EGL_NO_DISPLAY) {
        EGL14.eglMakeCurrent(
          eglDisplay,
          EGL14.EGL_NO_SURFACE,
          EGL14.EGL_NO_SURFACE,
          EGL14.EGL_NO_CONTEXT
        )
        if (eglSurface != EGL14.EGL_NO_SURFACE) {
          EGL14.eglDestroySurface(eglDisplay, eglSurface)
          eglSurface = EGL14.EGL_NO_SURFACE
        }
        if (eglContext != EGL14.EGL_NO_CONTEXT) {
          EGL14.eglDestroyContext(eglDisplay, eglContext)
          eglContext = EGL14.EGL_NO_CONTEXT
        }
        EGL14.eglTerminate(eglDisplay)
        eglDisplay = EGL14.EGL_NO_DISPLAY
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}
