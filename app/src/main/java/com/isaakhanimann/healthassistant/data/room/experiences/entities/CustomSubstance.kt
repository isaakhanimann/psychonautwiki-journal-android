package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class CustomSubstance(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    var units: String,
    var description: String,
)