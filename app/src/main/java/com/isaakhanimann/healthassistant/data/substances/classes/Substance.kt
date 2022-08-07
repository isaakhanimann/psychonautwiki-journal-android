package com.isaakhanimann.healthassistant.data.substances.classes

import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.Roa

data class Substance(
    val name: String,
    val commonNames: List<String>,
    val url: String,
    val tolerance: Tolerance?,
    val crossTolerances: List<String>,
    val addictionPotential: String?,
    val toxicities: List<String>,
    val categories: List<String>,
    val summary: String?,
    val effectsSummary: String?,
    val dosageRemark: String?,
    val generalRisks: String?,
    val longtermRisks: String?,
    val saferUse: List<String>,
    val interactions: Interactions?,
    val roas: List<Roa>,
) {
    fun getRoa(route: AdministrationRoute): Roa? {
        return roas.firstOrNull { it.route == route }
    }

    val isHallucinogen
        get() = categories.any {
            val hallucinogens = setOf(
                "hallucinogen",
                "psychedelic",
                "dissociative",
                "deliriant",
            )
            hallucinogens.contains(it.lowercase())
        }
    val isStimulant
        get() = categories.any {
            val stimulants = setOf(
                "stimulant",
            )
            stimulants.contains(it.lowercase())
        }
}