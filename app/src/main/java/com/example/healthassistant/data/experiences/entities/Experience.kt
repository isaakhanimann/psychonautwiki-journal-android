package com.example.healthassistant.data.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Experience(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val creationDate: Date,
    val text: String
    )
