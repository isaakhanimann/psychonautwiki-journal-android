/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.roa.DurationRange
import com.isaakhanimann.journal.data.substances.classes.roa.DurationUnits
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.DataForOneEffectLine
import java.time.Instant
import java.time.temporal.ChronoUnit

class TimelinesPreviewProvider :
    PreviewParameterProvider<List<DataForOneEffectLine>> {
    override val values: Sequence<List<DataForOneEffectLine>> = sequenceOf(
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.SUBLINGUAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = null,
                    peak = null,
                    offset = null,
                    total = null,
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.SUBCUTANEOUS,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = null,
                    peak = null,
                    offset = null,
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.BUCCAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = null,
                    offset = null,
                    total = null,
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.INTRAVENOUS,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = null,
                    offset = null,
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.SMOKED,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2f,
                        units = DurationUnits.HOURS
                    ),
                    offset = null,
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2f,
                        units = DurationUnits.HOURS
                    ),
                    offset = null,
                    total = null,
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.INSUFFLATED,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2.5f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(4, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2.5f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 0.5f,
                horizontalWeight = 0.25f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(3, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.RED,
                startTime = Instant.now().minus(1, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name2",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = null,
                    comeup = null,
                    peak = null,
                    offset = null,
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = null
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.ORANGE,
                startTime = Instant.now(),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(3, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name2",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.PURPLE,
                startTime = Instant.now().minus(150, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name3",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BROWN,
                startTime = Instant.now().minus(130, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name4",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.ORANGE,
                startTime = Instant.now().minus(1, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name5",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.RED,
                startTime = Instant.now().minus(50, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "nam6",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.GREEN,
                startTime = Instant.now().minus(40, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name7",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.CYAN,
                startTime = Instant.now().minus(30, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name8",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(28, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name9",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.RED,
                startTime = Instant.now().minus(24, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name10",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.ORANGE,
                startTime = Instant.now().minus(20, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name11",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BROWN,
                startTime = Instant.now().minus(15, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name12",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.INDIGO,
                startTime = Instant.now().minus(10, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name13",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.RED,
                startTime = Instant.now().minus(5, ChronoUnit.MINUTES),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name14",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 4f,
                        max = 6f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 3f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 6f,
                        max = 11f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.GREEN,
                startTime = Instant.now(),
                endTime = null,
            )
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name15",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2.5f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(3, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name16",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2.5f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.GREEN,
                startTime = Instant.now(),
                endTime = null,
            ),
        ),
        listOf(
            DataForOneEffectLine(
                substanceName = "name15",
                route = AdministrationRoute.ORAL,
                roaDuration = RoaDuration(
                    onset = DurationRange(
                        min = 20f,
                        max = 40f,
                        units = DurationUnits.MINUTES
                    ),
                    comeup = DurationRange(
                        min = 15f,
                        max = 30f,
                        units = DurationUnits.MINUTES
                    ),
                    peak = DurationRange(
                        min = 1.5f,
                        max = 2.5f,
                        units = DurationUnits.HOURS
                    ),
                    offset = DurationRange(
                        min = 2f,
                        max = 4f,
                        units = DurationUnits.HOURS
                    ),
                    total = DurationRange(
                        min = 3f,
                        max = 5f,
                        units = DurationUnits.HOURS
                    ),
                    afterglow = DurationRange(
                        min = 12f,
                        max = 48f,
                        units = DurationUnits.HOURS
                    )
                ),
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.BLUE,
                startTime = Instant.now().minus(3, ChronoUnit.HOURS),
                endTime = null,
            ),
            DataForOneEffectLine(
                substanceName = "name16",
                route = AdministrationRoute.ORAL,
                roaDuration = null,
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.GREEN,
                startTime = Instant.now().minus(80, ChronoUnit.MINUTES),
                endTime = Instant.now().plus(100, ChronoUnit.MINUTES),
            ),
            DataForOneEffectLine(
                substanceName = "name17",
                route = AdministrationRoute.ORAL,
                roaDuration = null,
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.PINK,
                startTime = Instant.now().minus(40, ChronoUnit.MINUTES),
                endTime = Instant.now().plus(150, ChronoUnit.MINUTES),
            ),
            DataForOneEffectLine(
                substanceName = "name17",
                route = AdministrationRoute.ORAL,
                roaDuration = null,
                height = 1f,
                horizontalWeight = 0.5f,
                color = AdaptiveColor.PURPLE,
                startTime = Instant.now().plus(160, ChronoUnit.MINUTES),
                endTime = Instant.now().plus(200, ChronoUnit.MINUTES),
            ),
        )
    )
}