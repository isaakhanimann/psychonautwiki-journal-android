package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomSubstance(
    @PrimaryKey
    val name: String,
    var units: String,
    var description: String,
)