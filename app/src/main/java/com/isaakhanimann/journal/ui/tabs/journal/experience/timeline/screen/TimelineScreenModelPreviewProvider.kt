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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.screen

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRatingOption
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.utils.getInstant

class TimelineScreenModelPreviewProvider :
    PreviewParameterProvider<TimelineScreenModel> {

    override val values: Sequence<TimelineScreenModel> = sequenceOf(
        TimelineScreenModel(
            title = "Dave",
            dataForEffectLines = emptyList(),
            ratings = listOf(
                ShulginRating(
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 20,
                        hourOfDay = 1,
                        minute = 15
                    )!!,
                    creationDate = getInstant(
                        year = 2022,
                        month = 2,
                        day = 20,
                        hourOfDay = 1,
                        minute = 15
                    )!!,
                    option = ShulginRatingOption.TWO_PLUS,
                    experienceId = 0
                ),
                ShulginRating(
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 20,
                        hourOfDay = 3,
                        minute = 15
                    )!!,
                    creationDate = getInstant(
                        year = 2022,
                        month = 3,
                        day = 20,
                        hourOfDay = 1,
                        minute = 15
                    )!!,
                    option = ShulginRatingOption.PLUS,
                    experienceId = 0
                )
            ),
            timedNotes = listOf(
                TimedNote(
                    creationDate = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 25
                    )!!,
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 25
                    )!!,
                    note = "Onset start",
                    color = AdaptiveColor.PURPLE,
                    experienceId = 0,
                    isPartOfTimeline = true
                ),
                TimedNote(
                    creationDate = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 25
                    )!!,
                    time = getInstant(
                        year = 2022,
                        month = 2,
                        day = 19,
                        hourOfDay = 23,
                        minute = 45
                    )!!,
                    note = "Peak start and this is a note that spans multiple lines, so long that we can see what a bigger layout looks like.",
                    color = AdaptiveColor.BLUE,
                    experienceId = 0,
                    isPartOfTimeline = true
                )
            ),
            timeDisplayOption = TimeDisplayOption.REGULAR,
            areSubstanceHeightsIndependent = false
        )
    )
}