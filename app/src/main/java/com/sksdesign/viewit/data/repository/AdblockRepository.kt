package com.sksdesign.viewit.data.repository

import android.content.Context
import com.sksdesign.viewit.data.model.AdblockSettings
import com.sksdesign.viewit.data.storage.JsonStorage
import com.sksdesign.viewit.data.storage.PreferencesStore

class AdblockRepository(context: Context) {
    private val store = PreferencesStore(context)

    fun load(): AdblockSettings {
        val raw = store.getString(KEY_ADBLOCK, "")
        return if (raw.isBlank()) AdblockSettings() else runCatching { AdblockSettings.fromJson(JsonStorage.objectOrEmpty(raw)) }.getOrDefault(AdblockSettings())
    }

    fun save(settings: AdblockSettings) {
        store.putString(KEY_ADBLOCK, settings.toJson().toString())
    }

    fun exportRules(): String = load().userRules.joinToString("\n")

    fun importRules(raw: String): AdblockSettings {
        val rules = raw.lines().map { it.trim() }.filter { it.isNotBlank() }.distinct()
        val updated = load().copy(userRules = rules)
        save(updated)
        return updated
    }

    companion object {
        private const val KEY_ADBLOCK = "adblock_settings"
    }
}
