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

package com.isaakhanimann.journal.data.room.experiences.entities

enum class StomachFullness {
    EMPTY {
        override val text = "Empty"
        override val onsetDelayForOralInHours: Double = 0.0
    },
    HALF_FULL {
        override val text = "Half Full"
        override val onsetDelayForOralInHours = 1.5
    },
    FULL {
        override val text = "Full"
        override val onsetDelayForOralInHours: Double = 3.0
    },
    VERY_FULL {
        override val text = "Very Full"
        override val onsetDelayForOralInHours: Double = 4.0
    };

    abstract val text: String
    abstract val onsetDelayForOralInHours: Double
}