package com.example.healthassistant.data.experiences

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.healthassistant.data.experiences.entities.converters.DateConverter
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.converters.UUIDConverter

@Database(entities = [Experience::class], version = 1, exportSchema = false)
@TypeConverters(UUIDConverter::class, DateConverter::class)
abstract class ExperienceDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}