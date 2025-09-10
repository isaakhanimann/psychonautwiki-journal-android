package com.isaakhanimann.journal.data.room

import androidx.room.TypeConverter
import com.isaakhanimann.journal.data.room.experiences.entities.custom.CustomRoaInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    @TypeConverter
    fun fromRoaInfoList(roaInfos: List<CustomRoaInfo>): String {
        return json.encodeToString(roaInfos)
    }

    @TypeConverter
    fun toRoaInfoList(roaInfosString: String): List<CustomRoaInfo> {
        if (roaInfosString.isBlank() || roaInfosString == "[]") {
            return emptyList()
        }
        return json.decodeFromString(roaInfosString)
    }
}