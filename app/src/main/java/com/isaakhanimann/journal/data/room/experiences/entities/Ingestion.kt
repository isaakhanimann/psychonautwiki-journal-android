package com.isaakhanimann.journal.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import kotlinx.serialization.Serializable
import java.time.Instant

@Entity
@Serializable
data class Ingestion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val substanceName: String,
    @Serializable(with=InstantSerializer::class) var time: Instant,
    val administrationRoute: AdministrationRoute,
    var dose: Double?,
    var isDoseAnEstimate: Boolean,
    var units: String?,
    var experienceId: Int,
    var notes: String?
)
