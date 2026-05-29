package com.sksdesign.viewit.data.model

data class BrowserControlSettings(
    val thirdPartyCookies: Boolean = false,
    val mediaPlaybackWithoutGesture: Boolean = true,
    val cacheMode: String = "default",
    val defaultDesktopMode: Boolean = false,
    val defaultJavaScript: Boolean = true,
    val autoHideControls: Boolean = true
)
