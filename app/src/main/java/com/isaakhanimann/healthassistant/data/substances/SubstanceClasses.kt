package com.isaakhanimann.healthassistant.data.substances

import androidx.compose.ui.graphics.Color

enum class InteractionType {
    DANGEROUS {
        override val color: Color
            get() = Color.Red
    },
    UNSAFE {
        override val color: Color
            get() = Color(0xFFFF9800)
    },
    UNCERTAIN {
        override val color: Color
            get() = Color.Yellow
    };

    abstract val color: Color
}

data class Substance(
    val name: String,
    val commonNames: List<String>,
    val url: String,
    val chemicalClasses: List<String>,
    val psychoactiveClasses: List<String>,
    val tolerance: Tolerance?,
    val roas: List<Roa>,
    val addictionPotential: String?,
    val toxicities: List<String>,
    val crossTolerances: List<String>,
    val uncertainInteractions: List<String>,
    val unsafeInteractions: List<String>,
    val dangerousInteractions: List<String>
) {
    fun getRoa(route: AdministrationRoute): Roa? {
        return roas.firstOrNull { it.route == route }
    }

    fun getInteractions(interactionType: InteractionType): List<String> {
        return when (interactionType) {
            InteractionType.DANGEROUS -> dangerousInteractions
            InteractionType.UNSAFE -> unsafeInteractions
            InteractionType.UNCERTAIN -> uncertainInteractions
        }
    }

    val isHallucinogen
        get() = psychoactiveClasses.any {
            val hallucinogens = setOf(
                "hallucinogens",
                "hallucinogen",
                "psychedelics",
                "psychedelic",
                "dissociatives",
                "dissociative",
                "deliriant",
                "deliriants"
            )
            hallucinogens.contains(it.lowercase())
        }
    val isStimulant
        get() = psychoactiveClasses.any {
            val stimulants = setOf(
                "stimulant",
                "stimulants"
            )
            stimulants.contains(it.lowercase())
        }
}

data class Tolerance(
    val full: String?,
    val half: String?,
    val zero: String?
)

data class Roa(
    val route: AdministrationRoute,
    val roaDose: RoaDose?,
    val roaDuration: RoaDuration?,
    val bioavailability: Bioavailability?
)

data class RoaDose(
    val units: String?,
    val threshold: Double?,
    val light: DoseRange?,
    val common: DoseRange?,
    val strong: DoseRange?,
    val heavy: Double?
) {
    fun getDoseClass(ingestionDose: Double?, ingestionUnits: String? = units): DoseClass? {
        if (ingestionUnits != units || ingestionDose == null) return null
        return if (threshold != null && ingestionDose < threshold) {
            DoseClass.THRESHOLD
        } else if (light?.isValueInRange(ingestionDose) == true) {
            DoseClass.LIGHT
        } else if (common?.isValueInRange(ingestionDose) == true) {
            DoseClass.COMMON
        } else if (strong?.isValueInRange(ingestionDose) == true) {
            DoseClass.STRONG
        } else if (heavy != null && ingestionDose > heavy) {
            DoseClass.HEAVY
        } else {
            null
        }
    }

    val shouldDefinitelyUseVolumetricDosing: Boolean
        get() {
            if (units == "µg") return true
            return if (units == "mg") {
                val sample = common?.min
                sample != null && sample < 15
            } else {
                false
            }
        }
}

enum class DoseClass {
    THRESHOLD {
        override val numDots: Int
            get() = 1
        override val description: String
            get() = "A threshold dose is the dose at which the mental and physical alterations produced by the substance first become apparent.\n" +
                    "These effects are distinctly beyond that of placebo but may still be ignored with some effort by directing one's focus towards the external environment.\n" +
                    "Subjects may perceive a vague sense of \"something\" or anticipatory energy building up in the background at this level."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 100, green = 210, blue = 255)
            } else {
                Color(red = 50, green = 173, blue = 230)
            }
        }
    },
    LIGHT {
        override val numDots: Int
            get() = 2
        override val description: String
            get() = "A light dose produces a state which is somewhat distinct from sobriety but does not threaten to override the subject's ordinary awareness.\n" +
                    "The effects can be ignored by increasing the focus one directs towards the external environment and performing complex tasks.\n" +
                    "The subject may have to pay particular attention for the substance's effects to be perceptible, or they may be slightly noticeable but will not insist upon the subject's attention."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 48, green = 209, blue = 88)
            } else {
                Color(red = 52, green = 199, blue = 89)
            }
        }
    },
    COMMON {
        override val numDots: Int
            get() = 3
        override val description: String
            get() = "A common dose is the dose at which the effects and nature of the substance is quite clear and distinct; the subject's ordinary awareness slips and ignoring its action becomes difficult.\n" +
                    "The subject will generally be able to partake in regular behaviors and remain functional and able to communicate, although this can depend on the individual.\n" +
                    "The effects can be allowed to occupy a predominant role or they may be suppressed and made secondary to other chosen activities with sufficient effort or in case of an emergency."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 214, blue = 10)
            } else {
                Color(red = 255, green = 204, blue = 0)
            }
        }
    },
    STRONG {
        override val numDots: Int
            get() = 4
        override val description: String
            get() = "A strong dose renders its subject mostly incapable of functioning, interacting normally, or thinking in a straightforward manner.\n" +
                    "The effects of the substance are clear and can no longer be ignored or suppressed, leaving the subject entirely engaged in the experience regardless of their desire or volition. Negative effects become more common at this level.\n" +
                    "As subjects are not able to alter the trajectory of their behavior at strong doses, it is vital that they have prepared their environment and activities in advance as well as taken any precautionary measures."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 159, blue = 10)
            } else {
                Color(red = 255, green = 149, blue = 0)
            }
        }
    },
    HEAVY {
        override val numDots: Int
            get() = 5
        override val description: String
            get() = "A heavy dose is the upper limit of what a substance is capable of producing in terms of psychoactive effects; doses past this range are associated with rapidly increasing side effects and marginal desirable effects.\n" +
                    "Depending on the substance consumed, the user may be rendered incapable of functioning and communicating in addition to experiencing extremely uncomfortable side effects that overshadow the positive effects experienced at lower doses.\n" +
                    "It is absolutely vital to employ harm reduction measures with heavy doses as the user will likely be unable to properly take care of themselves in the event of an emergency. Trip sitters are strongly advised.\n" +
                    "Users should also be aware that the line between a heavy dose and overdose is often very blurry and they are placing themselves at a significantly higher risk of injury, hospitalization, and death whenever they choose to take a heavy dose.\n" +
                    "The desire or compulsion to regularly take heavy doses (\"chronic use\") may also be an indicator of tolerance, addiction or other mental health problems."

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 69, blue = 58)
            } else {
                Color(red = 255, green = 59, blue = 48)
            }
        }
    };

    abstract val numDots: Int
    abstract val description: String
    abstract fun getComposeColor(isDarkTheme: Boolean): Color
}

data class DoseRange(
    val min: Double?,
    val max: Double?
) {
    fun isValueInRange(value: Double): Boolean {
        if (min == null || max == null) return false
        return value in min.rangeTo(max)
    }
}

data class RoaDuration(
    val onset: DurationRange?,
    val comeup: DurationRange?,
    val peak: DurationRange?,
    val offset: DurationRange?,
    val total: DurationRange?,
    val afterglow: DurationRange?
)

data class DurationRange(
    val min: Float?,
    val max: Float?,
    val units: DurationUnits?
) {
    val text
        get() = "${min.toString().removeSuffix(".0")}-${
            max.toString().removeSuffix(".0")
        } ${units?.text ?: ""}"

    val minInSec: Float? = if (units != null) min?.times(units.inSecondsMultiplier) else null
    val maxInSec: Float? = if (units != null) max?.times(units.inSecondsMultiplier) else null

    fun interpolateAtValueInSeconds(value: Float): Float? {
        if (min == null || max == null || units == null) return null
        val diff = max - min
        val withUnit = min + diff.times(value)
        return withUnit.times(units.inSecondsMultiplier)
    }
}

enum class DurationUnits(val text: String) {
    SECONDS("seconds") {
        override val inSecondsMultiplier: Int
            get() = 1
    },
    MINUTES("minutes") {
        override val inSecondsMultiplier: Int
            get() = 60
    },
    HOURS("hours") {
        override val inSecondsMultiplier: Int
            get() = 3600
    },
    DAYS("days") {
        override val inSecondsMultiplier: Int
            get() = 86400
    };

    abstract val inSecondsMultiplier: Int
}

data class Bioavailability(
    val min: Double?,
    val max: Double?
)