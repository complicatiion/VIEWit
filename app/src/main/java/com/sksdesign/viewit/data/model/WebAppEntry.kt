package com.sksdesign.viewit.data.model

import org.json.JSONObject
import java.util.UUID

data class WebAppEntry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val url: String,
    val category: String = "Web",
    val iconStyle: String = "web",
    val iconColor: String = "#FFFFFF",
    val customUserAgent: String = "",
    val javascriptEnabled: Boolean = true,
    val desktopMode: Boolean = false,
    val mobileUrl: String = "",
    val desktopUrl: String = "",
    val adblockEnabled: Boolean = true,
    val cookiesEnabled: Boolean = true,
    val domStorageEnabled: Boolean = true,
    val cacheEnabled: Boolean = true,
    val favorite: Boolean = false,
    val builtIn: Boolean = false,
    val sortOrder: Int = 0
) {
    fun effectiveUrl(useTvLayout: Boolean = false): String {
        val useDesktopTarget = useTvLayout || desktopMode
        if (id == "youtube") {
            val selected = if (useTvLayout) desktopUrl.ifBlank { url } else url
            return selected.ifBlank { desktopUrl.ifBlank { mobileUrl } }
        }
        val selected = if (useDesktopTarget) desktopUrl.ifBlank { url } else mobileUrl.ifBlank { url }
        return selected.ifBlank { url }
    }

    fun usesDesktopPresentation(useTvLayout: Boolean = false): Boolean = useTvLayout || desktopMode

    fun toJson(): JSONObject = JSONObject()
        .put("id", id)
        .put("name", name)
        .put("url", url)
        .put("category", category)
        .put("iconStyle", iconStyle)
        .put("iconColor", iconColor)
        .put("customUserAgent", customUserAgent)
        .put("javascriptEnabled", javascriptEnabled)
        .put("desktopMode", desktopMode)
        .put("mobileUrl", mobileUrl)
        .put("desktopUrl", desktopUrl)
        .put("adblockEnabled", adblockEnabled)
        .put("cookiesEnabled", cookiesEnabled)
        .put("domStorageEnabled", domStorageEnabled)
        .put("cacheEnabled", cacheEnabled)
        .put("favorite", favorite)
        .put("builtIn", builtIn)
        .put("sortOrder", sortOrder)

    companion object {
        private data class BuiltInDefaults(
            val id: String,
            val name: String,
            val url: String,
            val mobileUrl: String,
            val desktopUrl: String,
            val category: String,
            val iconStyle: String,
            val sortOrder: Int
        )

        private val builtIns = listOf(
            BuiltInDefaults(
                id = "youtube",
                name = "YouTube",
                url = "https://www.youtube.com",
                mobileUrl = "https://m.youtube.com",
                desktopUrl = "https://www.youtube.com",
                category = "Video",
                iconStyle = "youtube",
                sortOrder = 0
            ),
            BuiltInDefaults(
                id = "youtube_music",
                name = "YouTube Music",
                url = "https://music.youtube.com",
                mobileUrl = "https://music.youtube.com",
                desktopUrl = "https://music.youtube.com",
                category = "Music",
                iconStyle = "music",
                sortOrder = 1
            ),
            BuiltInDefaults(
                id = "soundcloud",
                name = "SoundCloud",
                url = "https://m.soundcloud.com",
                mobileUrl = "https://m.soundcloud.com",
                desktopUrl = "https://soundcloud.com",
                category = "Music",
                iconStyle = "soundcloud",
                sortOrder = 2
            ),
            BuiltInDefaults(
                id = "spotify",
                name = "Spotify",
                url = "https://open.spotify.com",
                mobileUrl = "https://open.spotify.com",
                desktopUrl = "https://open.spotify.com",
                category = "Music",
                iconStyle = "spotify",
                sortOrder = 3
            )
        )

        fun fromJson(json: JSONObject): WebAppEntry {
            val mainUrl = json.optString("url")
            return WebAppEntry(
                id = json.optString("id", UUID.randomUUID().toString()),
                name = json.optString("name"),
                url = mainUrl,
                category = json.optString("category", "Web"),
                iconStyle = json.optString("iconStyle", "web"),
                iconColor = json.optString("iconColor", "#FFFFFF"),
                customUserAgent = json.optString("customUserAgent", ""),
                javascriptEnabled = json.optBoolean("javascriptEnabled", true),
                desktopMode = json.optBoolean("desktopMode", false),
                mobileUrl = json.optString("mobileUrl", ""),
                desktopUrl = json.optString("desktopUrl", ""),
                adblockEnabled = json.optBoolean("adblockEnabled", true),
                cookiesEnabled = json.optBoolean("cookiesEnabled", true),
                domStorageEnabled = json.optBoolean("domStorageEnabled", true),
                cacheEnabled = json.optBoolean("cacheEnabled", true),
                favorite = json.optBoolean("favorite", false),
                builtIn = json.optBoolean("builtIn", false),
                sortOrder = json.optInt("sortOrder", 0)
            )
        }

        fun normalizedForCurrentVersion(entry: WebAppEntry, resetDesktopMode: Boolean = false): WebAppEntry {
            val match = builtInMatch(entry) ?: return if (resetDesktopMode && entry.builtIn) entry.copy(desktopMode = false) else entry
            val shouldResetDesktop = resetDesktopMode || entry.builtIn || entry.id == match.id
            return entry.copy(
                id = if (entry.builtIn || entry.id == match.id) match.id else entry.id,
                name = match.name,
                url = match.url,
                mobileUrl = match.mobileUrl,
                desktopUrl = match.desktopUrl,
                category = match.category,
                iconStyle = match.iconStyle,
                customUserAgent = "",
                desktopMode = if (shouldResetDesktop) false else entry.desktopMode,
                builtIn = true,
                sortOrder = if (entry.sortOrder < 0) match.sortOrder else entry.sortOrder
            )
        }

        private fun builtInMatch(entry: WebAppEntry): BuiltInDefaults? {
            val id = entry.id.lowercase()
            val name = entry.name.lowercase()
            val key = "$id $name ${entry.url} ${entry.mobileUrl} ${entry.desktopUrl}".lowercase()
            return when {
                id == "youtube" || ("youtube" in name && "music" !in name) || ("youtube" in key && "music.youtube" !in key && "youtube_music" !in key && "youtube music" !in key) -> builtIns.first { it.id == "youtube" }
                id == "youtube_music" || "youtube music" in name || "music.youtube" in key || "youtube_music" in key -> builtIns.first { it.id == "youtube_music" }
                id == "soundcloud" || "soundcloud" in key -> builtIns.first { it.id == "soundcloud" }
                id == "spotify" || "spotify" in key -> builtIns.first { it.id == "spotify" }
                else -> null
            }
        }

        fun defaults(): List<WebAppEntry> = builtIns.map { item ->
            WebAppEntry(
                id = item.id,
                name = item.name,
                url = item.url,
                mobileUrl = item.mobileUrl,
                desktopUrl = item.desktopUrl,
                category = item.category,
                iconStyle = item.iconStyle,
                javascriptEnabled = true,
                adblockEnabled = true,
                desktopMode = false,
                builtIn = true,
                sortOrder = item.sortOrder
            )
        }
    }
}
