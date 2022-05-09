package com.example.healthassistant.data.substances

import org.json.JSONArray
import org.json.JSONObject

fun JSONObject.getOptionalJSONObject(name: String): JSONObject? {
    return if (isNull(name)) {
        null
    } else {
        getJSONObject(name)
    }
}

fun JSONObject.getOptionalString(name: String): String? {
    return if (isNull(name)) {
        null
    } else {
        getString(name)
    }
}

fun JSONObject.getOptionalJSONArray(name: String): JSONArray? {
    return if (isNull(name)) {
        null
    } else {
        getJSONArray(name)
    }
}

fun JSONObject.getOptionalDouble(name: String): Double? {
    return if (isNull(name)) {
        null
    } else {
        getDouble(name)
    }
}

fun JSONArray.getOptionalString(index: Int): String? {
    return if (isNull(index)) {
        null
    } else {
        getString(index)
    }
}