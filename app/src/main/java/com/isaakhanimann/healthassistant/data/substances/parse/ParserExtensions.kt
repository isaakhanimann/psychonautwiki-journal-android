package com.isaakhanimann.healthassistant.data.substances.parse

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getOptionalJSONObject(name: String): JSONObject? {
    return try {
        getJSONObject(name)
    } catch (e: Exception) {
        null
    }
}

fun JSONObject.getOptionalString(name: String): String? {
    return try {
        if (isNull(name))
            null
        else
            getString(name)
    } catch (e: Exception) {
        null
    }
}

fun JSONObject.getOptionalJSONArray(name: String): JSONArray? {
    return try {
        getJSONArray(name)
    } catch (e: Exception) {
        null
    }
}

fun JSONObject.getOptionalDouble(name: String): Double? {
    return try {
        getDouble(name)
    } catch (e: Exception) {
        null
    }
}

fun JSONArray.getOptionalString(index: Int): String? {
    return try {
        if (isNull(index))
            null
        else
            getString(index)
    } catch (e: Exception) {
        null
    }
}

fun JSONArray.getOptionalJSONObject(index: Int): JSONObject? {
    return try {
        getJSONObject(index)
    } catch (e: Exception) {
        null
    }
}