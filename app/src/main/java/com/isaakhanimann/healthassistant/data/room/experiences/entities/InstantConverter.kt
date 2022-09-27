package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {
    @TypeConverter
    fun toLong(instant: Instant?): Long? {
        return instant?.epochSecond
    }

    @TypeConverter
    fun fromLong(value: Long?): Instant? {
        return value?.let { Instant.ofEpochSecond(it) }
    }
}