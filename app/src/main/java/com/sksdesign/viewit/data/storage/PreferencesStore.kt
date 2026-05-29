package com.sksdesign.viewit.data.storage

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {
    private val prefs: SharedPreferences = context.applicationContext.getSharedPreferences("viewit_preferences", Context.MODE_PRIVATE)

    fun getString(key: String, fallback: String = ""): String = prefs.getString(key, fallback) ?: fallback
    fun putString(key: String, value: String) = prefs.edit().putString(key, value).apply()
    fun getBoolean(key: String, fallback: Boolean = false): Boolean = prefs.getBoolean(key, fallback)
    fun putBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()
    fun getFloat(key: String, fallback: Float = 0f): Float = prefs.getFloat(key, fallback)
    fun putFloat(key: String, value: Float) = prefs.edit().putFloat(key, value).apply()
    fun getLong(key: String, fallback: Long = 0L): Long = prefs.getLong(key, fallback)
    fun putLong(key: String, value: Long) = prefs.edit().putLong(key, value).apply()
    fun clear() = prefs.edit().clear().apply()
}
