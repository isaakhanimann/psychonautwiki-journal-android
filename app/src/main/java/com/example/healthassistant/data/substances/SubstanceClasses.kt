package com.example.healthassistant.data.substances

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
)

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