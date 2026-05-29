package com.sksdesign.viewit.adblock

object RequestClassifier {
    fun classify(url: String): String = when {
        url.contains(".js", ignoreCase = true) -> "script"
        url.contains(".css", ignoreCase = true) -> "stylesheet"
        url.contains(".png", ignoreCase = true) || url.contains(".jpg", ignoreCase = true) || url.contains(".webp", ignoreCase = true) -> "image"
        url.contains("doubleclick", ignoreCase = true) || url.contains("ads", ignoreCase = true) -> "ad"
        else -> "other"
    }
}
