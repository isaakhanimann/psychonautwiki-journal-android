package com.example.healthassistant.data.substances

import androidx.compose.ui.graphics.Color
import kotlin.time.Duration

enum class InteractionType {
    DANGEROUS, UNSAFE, UNCERTAIN
}

data class Substance(
    val name: String,
    val commonNames: List<String>,
    val url: String,
    val effects: List<Effect>,
    val chemicalClasses: List<String>,
    val psychoactiveClasses: List<String>,
    val tolerance: Tolerance?,
    val roas: List<Roa>,
    val addictionPotential: String?,
    val toxicity: String?,
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
}

data class Effect(
    val name: String,
    val url: String
)

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
}

enum class DoseClass {
    THRESHOLD {
        override val numDots: Int
            get() = 1

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

        override fun getComposeColor(isDarkTheme: Boolean): Color {
            return if (isDarkTheme) {
                Color(red = 255, green = 69, blue = 58)
            } else {
                Color(red = 255, green = 59, blue = 48)
            }
        }
    };

    abstract val numDots: Int
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
    val min: Duration?,
    val max: Duration?
) {
    val text
        get() = "${min.toString().filterNot { it.isWhitespace() }}-${
            max.toString().filterNot { it.isWhitespace() }
        }"

    fun interpolateAt(value: Double): Duration? {
        if (min == null || max == null) return null
        val diff = max - min
        return min + diff.times(value)
    }
}

data class Bioavailability(
    val min: Double?,
    val max: Double?
)

enum class AdministrationRoute {
    ORAL {
        override val displayText: String
            get() = "Oral"
        override val description: String
            get() = "Swallowed"
    },
    SUBLINGUAL {
        override val displayText: String
            get() = "Sublingual"
        override val description: String
            get() = "Under Tongue"
    },
    BUCCAL {
        override val displayText: String
            get() = "Buccal"
        override val description: String
            get() = "Between Gums and Cheek"
    },
    INSUFFLATED {
        override val displayText: String
            get() = "Insufflated"
        override val description: String
            get() = "Sniffed"
    },
    RECTAL {
        override val displayText: String
            get() = "Rectal"
        override val description: String
            get() = "Boofing / Plugging"
    },
    TRANSDERMAL {
        override val displayText: String
            get() = "Transdermal"
        override val description: String
            get() = "Through Skin"
    },
    SUBCUTANEOUS {
        override val displayText: String
            get() = "Subcutaneous"
        override val description: String
            get() = "Injected Between Skin and Muscle"
    },
    INTRAMUSCULAR {
        override val displayText: String
            get() = "Intramuscular"
        override val description: String
            get() = "Injected Into Muscle Tissue"
    },
    INTRAVENOUS {
        override val displayText: String
            get() = "Intravenous"
        override val description: String
            get() = "Injected Into Vein"
    },
    SMOKED {
        override val displayText: String
            get() = "Smoked"
        override val description: String
            get() = "Inhaling With Heat Source"
    },
    INHALED {
        override val displayText: String
            get() = "Inhaled"
        override val description: String
            get() = "Inhaling Without Heat Source"
    };

    abstract val displayText: String
    abstract val description: String

}