package com.example.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.healthassistant.data.substances.AdministrationRoute
import java.util.*

@Entity
data class Ingestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val substanceName: String,
    val time: Date,
    val administrationRoute: AdministrationRoute,
    val dose: Double?,
    val isDoseAnEstimate: Boolean,
    val units: String?,
    var experienceId: Int?,
    var notes: String?,
    var sentiment: Sentiment?
)
