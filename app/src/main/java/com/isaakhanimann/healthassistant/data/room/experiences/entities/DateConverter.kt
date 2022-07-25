package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toLong(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLong(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}