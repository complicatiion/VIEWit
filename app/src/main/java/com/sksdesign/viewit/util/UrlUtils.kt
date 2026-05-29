package com.sksdesign.viewit.util

import android.webkit.URLUtil

object UrlUtils {
    fun normalizeUrl(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return ""
        return when {
            trimmed.startsWith("https://", ignoreCase = true) -> trimmed
            trimmed.startsWith("http://", ignoreCase = true) -> trimmed
            else -> "https://$trimmed"
        }
    }

    fun validateHttpUrl(input: String): Result<String> = runCatching {
        val normalized = normalizeUrl(input)
        require(normalized.isNotBlank()) { "URL must not be empty." }
        require(normalized.startsWith("http://") || normalized.startsWith("https://")) { "Only http and https URLs are supported." }
        require(URLUtil.isValidUrl(normalized)) { "The URL is not valid." }
        normalized
    }

    fun isHttpWarning(url: String): Boolean = url.startsWith("http://", ignoreCase = true)
}
