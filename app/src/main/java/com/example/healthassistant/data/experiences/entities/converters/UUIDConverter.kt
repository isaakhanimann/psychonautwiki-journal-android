package com.example.healthassistant.data.experiences.entities.converters

import androidx.room.TypeConverter
import java.util.*

class UUIDConverter {
    @TypeConverter
    fun toString(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun fromString(string: String?): UUID? {
        return UUID.fromString(string)
    }
}