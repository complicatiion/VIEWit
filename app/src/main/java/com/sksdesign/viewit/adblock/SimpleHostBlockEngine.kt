package com.sksdesign.viewit.adblock

import android.net.Uri
import java.util.Locale

class SimpleHostBlockEngine(
    private var enabled: Boolean = true,
    initialRules: List<String> = emptyList()
) : AdblockEngine {
    private val defaultRules = mutableSetOf(
        "doubleclick.net",
        "googlesyndication.com",
        "googleadservices.com",
        "adservice.google.com",
        "pagead2.googlesyndication.com",
        "tracking",
        "analytics"
    )
    private val userRules = initialRules.map { it.trim().lowercase(Locale.US) }.filter { it.isNotBlank() }.toMutableSet()

    override fun isEnabled(): Boolean = enabled

    fun setEnabled(value: Boolean) {
        enabled = value
    }

    override fun shouldBlock(url: String, resourceType: String?): Boolean {
        if (!enabled) return false
        val normalizedUrl = url.lowercase(Locale.US)
        val host = runCatching { Uri.parse(url).host?.lowercase(Locale.US).orEmpty() }.getOrDefault("")
        val allRules = defaultRules + userRules
        return allRules.any { rule ->
            val clean = rule.removePrefix("||").removePrefix(".").trim()
            clean.isNotBlank() && (host.contains(clean) || normalizedUrl.contains(clean))
        }
    }

    override fun updateLists() = Unit
    override fun addUserRule(rule: String) {
        val clean = rule.trim().lowercase(Locale.US)
        if (clean.isNotBlank()) userRules += clean
    }
    override fun removeUserRule(rule: String) {
        userRules -= rule.trim().lowercase(Locale.US)
    }
}
