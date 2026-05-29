package com.sksdesign.viewit.web

object UserAgentManager {
    private const val DesktopUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

    fun resolve(defaultUserAgent: String, desktopMode: Boolean, customUserAgent: String): String = when {
        customUserAgent.isNotBlank() -> customUserAgent
        desktopMode -> DesktopUserAgent
        else -> defaultUserAgent
    }
}
