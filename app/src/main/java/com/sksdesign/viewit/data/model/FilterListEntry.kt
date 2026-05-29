package com.sksdesign.viewit.data.model

import org.json.JSONObject

data class FilterListEntry(
    val id: String,
    val name: String,
    val url: String,
    val enabled: Boolean = false,
    val lastUpdated: Long = 0L
) {
    fun toJson(): JSONObject = JSONObject()
        .put("id", id)
        .put("name", name)
        .put("url", url)
        .put("enabled", enabled)
        .put("lastUpdated", lastUpdated)

    companion object {
        fun fromJson(json: JSONObject): FilterListEntry = FilterListEntry(
            id = json.optString("id"),
            name = json.optString("name"),
            url = json.optString("url"),
            enabled = json.optBoolean("enabled", false),
            lastUpdated = json.optLong("lastUpdated", 0L)
        )

        fun defaults(): List<FilterListEntry> = listOf(
            FilterListEntry("easylist", "EasyList", "https://easylist.to/easylist/easylist.txt", false),
            FilterListEntry("easyprivacy", "EasyPrivacy", "https://easylist.to/easylist/easyprivacy.txt", false),
            FilterListEntry("adguard_base", "AdGuard Base Filter", "https://filters.adtidy.org/extension/chromium/filters/2.txt", false)
        )
    }
}
