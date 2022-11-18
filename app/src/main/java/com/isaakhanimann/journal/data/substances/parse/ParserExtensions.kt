/*
 * Copyright (c) 2022. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
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