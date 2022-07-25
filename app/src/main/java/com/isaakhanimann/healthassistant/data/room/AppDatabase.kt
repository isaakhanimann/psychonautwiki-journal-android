package com.isaakhanimann.healthassistant.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceDao
import com.isaakhanimann.healthassistant.data.room.experiences.entities.DateConverter
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion

@Database(entities = [Experience::class, Ingestion::class, SubstanceCompanion::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}