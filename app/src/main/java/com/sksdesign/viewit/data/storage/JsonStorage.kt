package com.sksdesign.viewit.data.storage

import org.json.JSONArray
import org.json.JSONObject

object JsonStorage {
    fun arrayOrEmpty(raw: String): JSONArray = try {
        if (raw.isBlank()) JSONArray() else JSONArray(raw)
    } catch (_: Exception) {
        JSONArray()
    }

    fun objectOrEmpty(raw: String): JSONObject = try {
        if (raw.isBlank()) JSONObject() else JSONObject(raw)
    } catch (_: Exception) {
        JSONObject()
    }

    fun prettyObject(json: JSONObject): String = json.toString(2)
    fun prettyArray(json: JSONArray): String = json.toString(2)
}
