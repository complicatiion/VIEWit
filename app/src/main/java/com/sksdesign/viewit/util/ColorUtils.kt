package com.sksdesign.viewit.util

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun parseColor(hex: String, fallback: Color = Color.White): Color = runCatching {
        Color(android.graphics.Color.parseColor(hex))
    }.getOrDefault(fallback)

    fun normalizeHex(input: String): String {
        val value = input.trim()
        return when {
            value.matches(Regex("^#[0-9A-Fa-f]{6}$")) -> value.uppercase()
            value.matches(Regex("^[0-9A-Fa-f]{6}$")) -> "#${value.uppercase()}"
            else -> "#FFFFFF"
        }
    }
}
