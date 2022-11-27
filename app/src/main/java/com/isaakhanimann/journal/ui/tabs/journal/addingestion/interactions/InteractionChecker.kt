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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions

import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.classes.Substance
import com.isaakhanimann.journal.data.substances.repositories.SubstanceRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InteractionChecker @Inject constructor(
    private val substanceRepo: SubstanceRepository,
) {
    fun getInteractionBetween(aName: String, bName: String): Interaction? {
        val interactionFromAToB = getInteractionFromAToB(aName, bName)
        val interactionFromBToA = getInteractionFromAToB(bName, aName)
        if (interactionFromAToB != null && interactionFromBToA != null) {
            val isAtoB = interactionFromAToB.dangerCount >= interactionFromBToA.dangerCount
            val interactionType = if (isAtoB) interactionFromAToB else interactionFromBToA
            return Interaction(
                aName = aName,
                bName = bName,
                interactionType = interactionType,
                isInteractionFromAToB = isAtoB
            )
        } else if (interactionFromAToB != null) {
            return Interaction(
                aName = aName,
                bName = bName,
                interactionType = interactionFromAToB,
                isInteractionFromAToB = true
            )
        } else if (interactionFromBToA != null) {
            return Interaction(
                aName = aName,
                bName = bName,
                interactionType = interactionFromBToA,
                isInteractionFromAToB = false
            )
        } else {
            return null
        }
    }

    private fun getInteractionFromAToB(
        aName: String,
        bName: String,
    ): InteractionType? {
        val substanceA = substanceRepo.getSubstance(aName)
        return if (substanceA != null) {
            val dangerousInteractions = substanceA.interactions?.dangerous ?: emptyList()
            val unsafeInteractions = substanceA.interactions?.unsafe ?: emptyList()
            val uncertainInteractions = substanceA.interactions?.uncertain ?: emptyList()
            val substanceB = substanceRepo.getSubstance(bName)
            if (substanceB != null) {
                if (doInteractionsContainSubstance(dangerousInteractions, substanceB)) {
                    InteractionType.DANGEROUS
                } else if (doInteractionsContainSubstance(unsafeInteractions, substanceB)) {
                    InteractionType.UNSAFE
                } else if (doInteractionsContainSubstance(uncertainInteractions, substanceB)) {
                    InteractionType.UNCERTAIN
                } else {
                    null
                }
            } else {
                if (dangerousInteractions.contains(bName)) {
                    InteractionType.DANGEROUS
                } else if (unsafeInteractions.contains(bName)) {
                    InteractionType.UNSAFE
                } else if (uncertainInteractions.contains(bName)) {
                    InteractionType.UNCERTAIN
                } else {
                    null
                }
            }
        } else {
            null
        }
    }

    private fun doInteractionsContainSubstance(
        interactions: List<String>,
        substance: Substance
    ): Boolean {
        val extendedInteractions = replaceSubstitutedAmphetaminesAndSerotoninReleasers(interactions)
        val isSubstanceInDangerClass =
            substance.categories.any { categoryName ->
                extendedInteractions.any { interactionName ->
                    interactionName.contains(categoryName, ignoreCase = true)
                }
            }
        val isWildCardMatch = extendedInteractions.map { interaction ->
            Regex(
                pattern = interaction.replace(
                    oldValue = "x",
                    newValue = "[\\S]*",
                    ignoreCase = true
                ),
                option = RegexOption.IGNORE_CASE
            ).matches(substance.name)
        }.any { it }
        return extendedInteractions.contains(substance.name) || isSubstanceInDangerClass || isWildCardMatch
    }

    private fun replaceSubstitutedAmphetaminesAndSerotoninReleasers(interactions: List<String>): List<String> {
        return interactions.flatMap { name ->
            when (name) {
                "Substituted amphetamines" -> {
                    return@flatMap substitutedAmphetamines
                }
                "Serotonin releasers" -> {
                    return@flatMap serotoninReleasers
                }
                else -> {
                    return@flatMap listOf(name)
                }
            }
        }.distinct()
    }

    private val serotoninReleasers = listOf(
        "MDMA",
        "MDA",
        "Mephedrone"
    )

    private val substitutedAmphetamines = listOf(
        "Amphetamine",
        "Methamphetamine",
        "Ethylamphetamine",
        "Propylamphetamine",
        "Isopropylamphetamine",
        "Bromo-DragonFLY",
        "Lisdexamfetamine",
        "Clobenzorex",
        "Dimethylamphetamine",
        "Selegiline",
        "Benzphetamine",
        "Ortetamine",
        "3-Methylamphetamine",
        "4-Methylamphetamine",
        "4-MMA",
        "Xylopropamine",
        "ß-methylamphetamine",
        "3-phenylmethamphetamine",
        "2-FA",
        "2-FMA",
        "2-FEA",
        "3-FA",
        "3-FMA",
        "3-FEA",
        "Fenfluramine",
        "Norfenfluramine",
        "4-FA",
        "4-FMA",
        "4-CA",
        "4-BA",
        "4-IA",
        "DCA",
        "4-HA",
        "4-HMA",
        "3,4-DHA",
        "OMA",
        "3-MA",
        "MMMA",
        "MMA",
        "PMA",
        "PMMA",
        "PMEA",
        "4-ETA",
        "TMA-2",
        "TMA-6",
        "4-MTA",
        "5-API",
        "Cathine",
        "Phenmetrazine",
        "3-FPM",
        "Prolintane"
    )

}

data class Interaction(
    val aName: String,
    val bName: String,
    val interactionType: InteractionType,
    val isInteractionFromAToB: Boolean,
)