package com.isaakhanimann.healthassistant.data.room.experiences.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
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

@Serializer(forClass = Instant::class)
@OptIn(ExperimentalSerializationApi::class)
object InstantSerializer : KSerializer<Instant> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeLong(value.toEpochMilli())
    override fun deserialize(decoder: Decoder) = Instant.ofEpochMilli(decoder.decodeLong())
}
