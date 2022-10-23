package com.isaakhanimann.healthassistant.data.substances.classes.roa

data class DurationRange(
    val min: Float?,
    val max: Float?,
    val units: DurationUnits?
) {
    val text
        get() = "${min.toString().removeSuffix(".0")}-${
            max.toString().removeSuffix(".0")
        }${units?.shortText ?: ""}"

    val minInSec: Float? = if (units != null) min?.times(units.inSecondsMultiplier) else null
    val maxInSec: Float? = if (units != null) max?.times(units.inSecondsMultiplier) else null

    fun interpolateAtValueInSeconds(value: Float): Float? {
        if (min == null || max == null || units == null) return null
        val diff = max - min
        val withUnit = min + diff.times(value)
        return withUnit.times(units.inSecondsMultiplier)
    }
}