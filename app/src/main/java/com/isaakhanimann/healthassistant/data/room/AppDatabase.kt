package com.isaakhanimann.healthassistant.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.isaakhanimann.healthassistant.data.room.experiences.ExperienceDao
import com.isaakhanimann.healthassistant.data.room.experiences.entities.*

@Database(entities = [Experience::class, Ingestion::class, SubstanceCompanion::class, CustomSubstance::class], version = 1, exportSchema = false)
@TypeConverters(InstantConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun experienceDao(): ExperienceDao
}