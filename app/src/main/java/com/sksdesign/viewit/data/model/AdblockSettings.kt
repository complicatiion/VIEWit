package com.sksdesign.viewit.data.model

import org.json.JSONArray
import org.json.JSONObject

data class AdblockSettings(
    val globalEnabled: Boolean = true,
    val useSimpleHostBlockEngine: Boolean = true,
    val useAdblockPlusEngine: Boolean = false,
    val userRules: List<String> = emptyList(),
    val filterLists: List<FilterListEntry> = FilterListEntry.defaults(),
    val blockedRequestCounter: Int = 0,
    val debugLogEnabled: Boolean = false,
    val lastUpdated: Long = 0L
) {
    fun toJson(): JSONObject = JSONObject()
        .put("globalEnabled", globalEnabled)
        .put("useSimpleHostBlockEngine", useSimpleHostBlockEngine)
        .put("useAdblockPlusEngine", useAdblockPlusEngine)
        .put("userRules", JSONArray(userRules))
        .put("filterLists", JSONArray(filterLists.map { it.toJson() }))
        .put("blockedRequestCounter", blockedRequestCounter)
        .put("debugLogEnabled", debugLogEnabled)
        .put("lastUpdated", lastUpdated)

    companion object {
        fun fromJson(json: JSONObject): AdblockSettings {
            val rules = json.optJSONArray("userRules")?.let { array ->
                (0 until array.length()).mapNotNull { array.optString(it).takeIf { value -> value.isNotBlank() } }
            } ?: emptyList()
            val filterLists = json.optJSONArray("filterLists")?.let { array ->
                (0 until array.length()).mapNotNull { index -> array.optJSONObject(index)?.let(FilterListEntry::fromJson) }
            } ?: FilterListEntry.defaults()
            return AdblockSettings(
                globalEnabled = json.optBoolean("globalEnabled", true),
                useSimpleHostBlockEngine = json.optBoolean("useSimpleHostBlockEngine", true),
                useAdblockPlusEngine = json.optBoolean("useAdblockPlusEngine", false),
                userRules = rules,
                filterLists = filterLists,
                blockedRequestCounter = json.optInt("blockedRequestCounter", 0),
                debugLogEnabled = json.optBoolean("debugLogEnabled", false),
                lastUpdated = json.optLong("lastUpdated", 0L)
            )
        }
    }
}
