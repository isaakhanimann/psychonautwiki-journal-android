package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity
data class Experience(
    @PrimaryKey
    val id: Int,
    var title: String,
    var text: String,
    val creationDate: Instant = Instant.now(),
    var isFavorite: Boolean = false
)
