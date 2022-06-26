package com.example.healthassistant.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.healthassistant.data.room.experiences.ExperienceDao
import com.example.healthassistant.data.room.experiences.entities.DateConverter
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.room.filter.FilterDao
import com.example.healthassistant.data.room.filter.SubstanceFilter

@Database(entities = [Experience::class, Ingestion::class, SubstanceFilter::class, SubstanceCompanion::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao

    abstract fun filterDao(): FilterDao
}