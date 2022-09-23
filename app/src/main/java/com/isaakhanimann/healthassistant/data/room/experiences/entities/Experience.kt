package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Experience(
    @PrimaryKey
    val id: Int,
    var title: String,
    var text: String,
    val creationDate: Date = Date(),
    var sentiment: Sentiment?
)
