package com.example.healthassistant.data.experiences.entities.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun toLong(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun fromLong(value: Long?): Date? {
        return value?.let { Date(it) }
    }
}