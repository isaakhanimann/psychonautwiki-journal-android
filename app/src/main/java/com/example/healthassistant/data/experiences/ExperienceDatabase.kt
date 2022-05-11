package com.example.healthassistant.data.experiences

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.healthassistant.data.experiences.entities.DateConverter
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.Ingestion

@Database(entities = [Experience::class, Ingestion::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class ExperienceDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}