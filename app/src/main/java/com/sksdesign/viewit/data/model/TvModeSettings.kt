package com.sksdesign.viewit.data.model

data class TvModeSettings(
    val deviceMode: DeviceMode = DeviceMode.MOBILE,
    val tvLayoutScale: String = "comfortable",
    val showTvControlOverlay: Boolean = true,
    val autoHideBrowserControls: Boolean = true,
    val focusGlowIntensity: Float = 0.65f,
    val startInLastWebApp: Boolean = false,
    val remoteBackBehavior: String = "webview_back_first",
    val dpadScrollSpeed: Float = 1.0f,
    val remoteInputEnabled: Boolean = true,
    val enableMediaKeys: Boolean = true,
    val showLargeCards: Boolean = true,
    val preferLandscapeLayout: Boolean = true
)
