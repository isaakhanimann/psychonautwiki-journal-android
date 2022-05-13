package com.example.healthassistant.data.substances

import kotlin.time.Duration

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
)

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
    val name: String,
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
)

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
    val text get() = "${min.toString()}-${max.toString()}"
}

data class Bioavailability(
    val min: Double?,
    val max: Double?
)