package com.sksdesign.viewit.device

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration

object DeviceModeDetector {
    fun isTelevision(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val configurationType = context.resources.configuration.uiMode and Configuration.UI_MODE_TYPE_MASK
        val pm = context.packageManager
        val leanback = pm.hasSystemFeature("android.software.leanback")
        val tvHardware = pm.hasSystemFeature("android.hardware.type.television")
        val uiModeIsTv = uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION || configurationType == Configuration.UI_MODE_TYPE_TELEVISION
        return uiModeIsTv || (leanback && tvHardware)
    }
}
