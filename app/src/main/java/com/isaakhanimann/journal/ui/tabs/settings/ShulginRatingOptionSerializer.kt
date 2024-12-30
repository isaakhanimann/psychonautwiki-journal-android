/*
 * Copyright (c) 2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.settings

import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ShulginRatingOptionSerializer : KSerializer<ShulginRatingOption> {
    override val descriptor = PrimitiveSerialDescriptor("ShulginRatingOption", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: ShulginRatingOption) {
        encoder.encodeString(value.rawValue)
    }

    override fun deserialize(decoder: Decoder): ShulginRatingOption {
        val ratingRawValue = decoder.decodeString()
        val foundRating = ShulginRatingOption.entries.firstOrNull {
            it.rawValue == ratingRawValue
        }
        return foundRating ?: ShulginRatingOption.FOUR_PLUS
    }
}