/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.data.substances.parse

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

fun JSONObject.getOptionalBoolean(name: String): Boolean? {
    return try {
        if (isNull(name))
            null
        else
            getBoolean(name)
    } catch (e: Exception) {
        null
    }
}

fun JSONObject.getOptionalLong(name: String): Long? {
    return try {
        if (isNull(name))
            null
        else
            getLong(name)
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