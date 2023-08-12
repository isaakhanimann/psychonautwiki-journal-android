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

package com.isaakhanimann.journal.ui.tabs.journal.experience.components.timednote

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import java.time.Instant

class TimedNotePreviewProvider : PreviewParameterProvider<TimedNote> {
    override val values: Sequence<TimedNote> = sequenceOf(
        TimedNote(
            time = Instant.now(),
            note = "Hello my name is",
            color = AdaptiveColor.PURPLE,
            experienceId = 0,
            isPartOfTimeline = true
        ),
        TimedNote(
            time = Instant.now(),
            note = "Hello my name is",
            color = AdaptiveColor.PURPLE,
            experienceId = 0,
            isPartOfTimeline = false
        )
    )
}