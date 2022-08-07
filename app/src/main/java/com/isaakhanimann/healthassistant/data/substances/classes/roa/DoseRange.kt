package com.isaakhanimann.healthassistant.data.substances.classes.roa

data class DoseRange(
    val min: Double?,
    val max: Double?
) {
    fun isValueInRange(value: Double): Boolean {
        if (min == null || max == null) return false
        return value in min.rangeTo(max)
    }
}