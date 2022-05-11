package com.example.healthassistant.data.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Ingestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val substanceName: String,
    val time: Date,
    val administrationRoute: String,
    val dose: Double,
    val units: String,
    val color: String,
    val experienceId: Int
)
