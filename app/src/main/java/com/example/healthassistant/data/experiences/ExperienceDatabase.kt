package com.example.healthassistant.data.experiences

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.healthassistant.data.experiences.entities.Experience

@Database(entities = [Experience::class], version = 1, exportSchema = false)
@TypeConverters(UUIDConverter::class)
abstract class ExperienceDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}