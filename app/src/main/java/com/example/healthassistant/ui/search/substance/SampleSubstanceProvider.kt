package com.example.healthassistant.ui.search.substance

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.substances.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class SampleSubstanceProvider : PreviewParameterProvider<Substance> {
    override val values: Sequence<Substance> = sequenceOf(
        Substance(
            name = "Example Substance",
            url = "https://psychonautwiki.org/wiki/Lsd",
            effects = listOf(
                Effect(
                    name = "Auditory enhancement",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Auditory distortion",
                    url = "https://psychonautwiki.org/wiki/Auditory_distortion"
                ),
                Effect(
                    name = "Geometry",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Pattern recognition enhancement",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Acuity enhancement",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Colour enhancement",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Diffraction",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Dehydration",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Nausea",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Physical euphoria",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
                Effect(
                    name = "Watery eyes",
                    url = "https://psychonautwiki.org/wiki/Auditory_enhancement"
                ),
            ),
            chemicalClasses = listOf("Amphetamines"),
            psychoactiveClasses = listOf("Entactogens"),
            tolerance = Tolerance(
                full = "with prolonged and repeated use",
                half = "1 month",
                zero = "2 months"
            ),
            roas = listOf(
                Roa(
                    name = "oral",
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
                            min = 20.0.toDuration(DurationUnit.MINUTES),
                            max = 40.0.toDuration(DurationUnit.MINUTES),
                        ),
                        comeup = DurationRange(
                            min = 15.0.toDuration(DurationUnit.MINUTES),
                            max = 30.0.toDuration(DurationUnit.MINUTES),
                        ),
                        peak = DurationRange(
                            min = 1.5.toDuration(DurationUnit.HOURS),
                            max = 2.5.toDuration(DurationUnit.HOURS),
                        ),
                        offset = DurationRange(
                            min = 1.0.toDuration(DurationUnit.HOURS),
                            max = 1.5.toDuration(DurationUnit.HOURS),
                        ),
                        total = DurationRange(
                            min = 3.0.toDuration(DurationUnit.HOURS),
                            max = 5.0.toDuration(DurationUnit.HOURS),
                        ),
                        afterglow = DurationRange(
                            min = 12.0.toDuration(DurationUnit.HOURS),
                            max = 48.0.toDuration(DurationUnit.HOURS),
                        )
                    ),
                    bioavailability = Bioavailability(
                        min = 70.0,
                        max = 75.0
                    )
                )
            ),
            addictionPotential = "moderately addictive with a high potential for abuse",
            toxicity = "toxic dose is unknown",
            crossTolerances = listOf(
                "Psychedelics",
                "Stimulants"
            ),
            uncertainInteractions = listOf(
                "MDMA",
                "Stimulants",
                "Dextromethorphan"
            ),
            unsafeInteractions = listOf(
                "Tramadol",
                "MAOI",
                "Dissociatives"
            ),
            dangerousInteractions = listOf(
                "Alcohol",
                "AMT",
                "Cocaine"
            )
        )
    )
}