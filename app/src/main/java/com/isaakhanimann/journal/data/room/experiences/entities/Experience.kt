package com.isaakhanimann.journal.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.Instant

@Entity
@Serializable
data class Experience(
    @PrimaryKey
    val id: Int,
    var title: String,
    var text: String,
    @Serializable(with=InstantSerializer::class) val creationDate: Instant = Instant.now(),
    @Serializable(with=InstantSerializer::class) val sortDate: Instant,
    var isFavorite: Boolean = false
)
