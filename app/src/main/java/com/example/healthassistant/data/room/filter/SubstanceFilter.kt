package com.example.healthassistant.data.room.filter

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SubstanceFilter(
    @PrimaryKey(autoGenerate = false)
    val substanceName: String
)
