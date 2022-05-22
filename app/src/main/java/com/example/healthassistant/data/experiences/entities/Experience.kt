package com.example.healthassistant.data.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Experience(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var date: Date,
    var text: String
    )
