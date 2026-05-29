package com.sksdesign.viewit.data.repository

import android.content.Context
import com.sksdesign.viewit.data.model.WebAppEntry
import com.sksdesign.viewit.data.storage.JsonStorage
import com.sksdesign.viewit.data.storage.PreferencesStore
import org.json.JSONArray
import java.util.UUID

class WebLibraryRepository(context: Context) {
    private val store = PreferencesStore(context)

    fun loadWebApps(): List<WebAppEntry> {
        val raw = store.getString(KEY_WEB_APPS, "")
        if (raw.isBlank()) return WebAppEntry.defaults()
        val parsed = JsonStorage.arrayOrEmpty(raw)
        val entries = (0 until parsed.length()).mapNotNull { index ->
            parsed.optJSONObject(index)?.let { runCatching { WebAppEntry.fromJson(it) }.getOrNull() }
        }
        if (entries.isEmpty()) return WebAppEntry.defaults()
        val resetDesktopMode = store.getLong(KEY_WEB_APPS_SCHEMA, 0L) < CURRENT_SCHEMA
        val normalized = entries.map { WebAppEntry.normalizedForCurrentVersion(it, resetDesktopMode) }.sortedBy { it.sortOrder }
        if (normalized != entries.sortedBy { it.sortOrder } || resetDesktopMode) {
            saveWebApps(normalized)
            store.putLong(KEY_WEB_APPS_SCHEMA, CURRENT_SCHEMA)
        }
        return normalized
    }

    fun saveWebApps(entries: List<WebAppEntry>) {
        val array = JSONArray(entries.sortedBy { it.sortOrder }.map { it.toJson() })
        store.putString(KEY_WEB_APPS, array.toString())
    }

    fun reset(): List<WebAppEntry> {
        val defaults = WebAppEntry.defaults()
        saveWebApps(defaults)
        return defaults
    }

    fun upsert(entry: WebAppEntry): List<WebAppEntry> {
        val current = loadWebApps().toMutableList()
        val index = current.indexOfFirst { it.id == entry.id }
        if (index >= 0) current[index] = entry else current += entry.copy(sortOrder = current.size)
        saveWebApps(current)
        return current
    }

    fun delete(entry: WebAppEntry): List<WebAppEntry> {
        val updated = loadWebApps().filterNot { it.id == entry.id && !it.builtIn }.mapIndexed { index, item -> item.copy(sortOrder = index) }
        saveWebApps(updated)
        return updated
    }

    fun duplicate(entry: WebAppEntry): List<WebAppEntry> {
        val current = loadWebApps().toMutableList()
        current += entry.copy(id = UUID.randomUUID().toString(), name = "${entry.name} Copy", builtIn = false, sortOrder = current.size)
        saveWebApps(current)
        return current
    }


    fun moveUp(entry: WebAppEntry): List<WebAppEntry> {
        val current = loadWebApps().toMutableList()
        val index = current.indexOfFirst { it.id == entry.id }
        if (index > 0) {
            val previous = current[index - 1]
            current[index - 1] = entry.copy(sortOrder = index - 1)
            current[index] = previous.copy(sortOrder = index)
            saveWebApps(current)
        }
        return loadWebApps()
    }

    fun moveDown(entry: WebAppEntry): List<WebAppEntry> {
        val current = loadWebApps().toMutableList()
        val index = current.indexOfFirst { it.id == entry.id }
        if (index >= 0 && index < current.lastIndex) {
            val next = current[index + 1]
            current[index] = next.copy(sortOrder = index)
            current[index + 1] = entry.copy(sortOrder = index + 1)
            saveWebApps(current)
        }
        return loadWebApps()
    }

    fun toggleFavorite(entry: WebAppEntry): List<WebAppEntry> = upsert(entry.copy(favorite = !entry.favorite))

    fun toggleAdblock(entry: WebAppEntry): List<WebAppEntry> = upsert(entry.copy(adblockEnabled = !entry.adblockEnabled))

    fun toggleDesktopMode(entry: WebAppEntry): List<WebAppEntry> = upsert(entry.copy(desktopMode = !entry.desktopMode))

    fun sortByName(): List<WebAppEntry> {
        val sorted = loadWebApps().sortedBy { it.name.lowercase() }.mapIndexed { index, item -> item.copy(sortOrder = index) }
        saveWebApps(sorted)
        return sorted
    }

    fun exportJson(): String = JsonStorage.prettyArray(JSONArray(loadWebApps().map { it.toJson() }))

    fun importJson(raw: String): Result<List<WebAppEntry>> = runCatching {
        val array = JSONArray(raw)
        val entries = (0 until array.length()).map { index -> WebAppEntry.fromJson(array.getJSONObject(index)) }
        require(entries.all { it.url.startsWith("http://") || it.url.startsWith("https://") }) { "Only http and https URLs are supported." }
        saveWebApps(entries.mapIndexed { index, item -> item.copy(sortOrder = index) })
        loadWebApps()
    }

    companion object {
        private const val KEY_WEB_APPS = "weblibrary_json"
        private const val KEY_WEB_APPS_SCHEMA = "weblibrary_schema"
        private const val CURRENT_SCHEMA = 12L
    }
}
