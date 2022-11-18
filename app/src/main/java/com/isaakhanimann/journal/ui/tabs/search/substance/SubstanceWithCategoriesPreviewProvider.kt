/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search.substance

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.*
import com.isaakhanimann.journal.data.substances.classes.roa.*

class SubstanceWithCategoriesPreviewProvider : PreviewParameterProvider<SubstanceWithCategories> {
    override val values: Sequence<SubstanceWithCategories> = sequenceOf(
        SubstanceWithCategories(
            substance = Substance(
                name = "Example Substance",
                commonNames = listOf("Hat", "Boot", "Hoodie", "Shirt", "Blouse"),
                url = "https://psychonautwiki.org/wiki/Lsd",
                isApproved = true,
                categories = listOf("entactogen, common"),
                tolerance = Tolerance(
                    full = "with prolonged and repeated use",
                    half = "1 month",
                    zero = "2 months"
                ),
                roas = listOf(
                    Roa(
                        route = AdministrationRoute.SMOKED,
                        roaDose = RoaDose(
                            "mg",
                            threshold = 20.0,
                            light = DoseRange(
                                min = 20.0,
                                max = 40.0
                            ),
                            common = DoseRange(
                                min = 40.0,
                                max = 90.0
                            ),
                            strong = DoseRange(
                                min = 90.0,
                                max = 140.0
                            ),
                            heavy = 140.0
                        ),
                        roaDuration = RoaDuration(
                            onset = DurationRange(
                                min = 10f,
                                max = 20f,
                                units = DurationUnits.MINUTES
                            ),
                            comeup = DurationRange(
                                min = 5f,
                                max = 15f,
                                units = DurationUnits.MINUTES
                            ),
                            peak = DurationRange(
                                min = 0.75f,
                                max = 1.25f,
                                units = DurationUnits.HOURS
                            ),
                            offset = DurationRange(
                                min = 0.5f,
                                max = 1f,
                                units = DurationUnits.HOURS
                            ),
                            total = DurationRange(
                                min = 1.5f,
                                max = 2.5f,
                                units = DurationUnits.HOURS
                            ),
                            afterglow = DurationRange(
                                min = 5f,
                                max = 20f,
                                units = DurationUnits.HOURS
                            )
                        ),
                        bioavailability = Bioavailability(
                            min = 70.0,
                            max = 75.0
                        )
                    ),
                    Roa(
                        route = AdministrationRoute.INTRAVENOUS,
                        roaDose = RoaDose(
                            "mg",
                            threshold = 20.0,
                            light = DoseRange(
                                min = 20.0,
                                max = 40.0
                            ),
                            common = DoseRange(
                                min = 40.0,
                                max = 90.0
                            ),
                            strong = DoseRange(
                                min = 90.0,
                                max = 140.0
                            ),
                            heavy = 140.0
                        ),
                        roaDuration = RoaDuration(
                            onset = DurationRange(
                                min = 5f,
                                max = 10f,
                                units = DurationUnits.SECONDS
                            ),
                            comeup = DurationRange(
                                min = 5f,
                                max = 10f,
                                units = DurationUnits.SECONDS
                            ),
                            peak = DurationRange(
                                min = 1f,
                                max = 2f,
                                units = DurationUnits.HOURS
                            ),
                            offset = DurationRange(
                                min = 1f,
                                max = 2f,
                                units = DurationUnits.HOURS
                            ),
                            total = DurationRange(
                                min = 2f,
                                max = 4f,
                                units = DurationUnits.HOURS
                            ),
                            afterglow = DurationRange(
                                min = 12f,
                                max = 48f,
                                units = DurationUnits.HOURS
                            )
                        ),
                        bioavailability = Bioavailability(
                            min = 70.0,
                            max = 75.0
                        )
                    ),
                    Roa(
                        route = AdministrationRoute.ORAL,
                        roaDose = RoaDose(
                            "mg",
                            threshold = 20.0,
                            light = DoseRange(
                                min = 20.0,
                                max = 40.0
                            ),
                            common = DoseRange(
                                min = 40.0,
                                max = 90.0
                            ),
                            strong = DoseRange(
                                min = 90.0,
                                max = 140.0
                            ),
                            heavy = 140.0
                        ),
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
                                min = 1f,
                                max = 1.5f,
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
                        bioavailability = Bioavailability(
                            min = 70.0,
                            max = 75.0
                        )
                    )
                ),
                addictionPotential = "moderately addictive with a high potential for abuse",
                toxicities = listOf(
                    "lethal if mixed with alcohol"
                ),
                crossTolerances = listOf(
                    "Psychedelics",
                    "Stimulants"
                ),
                interactions = Interactions(
                    dangerous = listOf(
                        "Alcohol",
                        "AMT",
                        "Cocaine"
                    ),
                    unsafe = listOf(
                        "Tramadol",
                        "MAOI",
                        "Dissociatives"
                    ),
                    uncertain = listOf(
                        "MDMA",
                        "Stimulants",
                        "Dextromethorphan"
                    ),
                ),
                summary = "This is a little summary of the substance.",
                effectsSummary = "This is a little summary of the effects.",
                dosageRemark = "This is a dosage remark",
                generalRisks = "Here are the risks associated with using this substance",
                longtermRisks = "Here are the longterm risks associated with using this substance",
                saferUse = listOf(
                    "this is tip number 1",
                    "this is tip number 2",
                    "this is tip number 3"
                )
            ),
            categories = listOf(
                Category(
                    name = "common",
                    description = "Common drugs are those which are well known and widely used among the drug community. This doesn't necessarily mean they are safe, but it usually comes with a longer relative history of use in humans with which to establish a safety profile.",
                    url = null,
                    color = Color(4278876927)
                ),
                Category(
                    name = "psychedelic",
                    description = "Psychedelics are drugs which alter the perception, causing a number of mental effects which manifest in many forms including altered states of consciousness, visual or tactile effects.",
                    url = "https://psychonautwiki.org/wiki/Psychedelics",
                    color = Color(4287564543)
                ),
                Category(
                    name = "stimulant",
                    description = "Stimulants excite the nervous system and increase physiological function.",
                    url = "https://psychonautwiki.org/wiki/Stimulants",
                    color = Color(4278246860)
                ),
                Category(
                    name = "entactogen",
                    description = "Entactogens (also known as empathogens) are a class of psychoactive substances that produce distinctive emotional and social effects similar to those of MDMA.",
                    url = "https://psychonautwiki.org/wiki/Entactogens",
                    color = Color(4294904442)
                )
            )
        )
    )
}