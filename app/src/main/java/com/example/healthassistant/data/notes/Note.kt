package com.example.healthassistant.data.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    val title: String
    )
