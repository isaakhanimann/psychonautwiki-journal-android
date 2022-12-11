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

package com.isaakhanimann.journal.data.substances.parse

import androidx.compose.ui.graphics.Color
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.*
import com.isaakhanimann.journal.data.substances.classes.roa.*
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceParser @Inject constructor() : SubstanceParserInterface {

    override fun parseSubstanceFile(string: String): SubstanceFile {
        val wholeFile = JSONObject(string)
        return SubstanceFile(
            categories = parseCategories(wholeFile),
            substances = parseSubstances(wholeFile)
        )
    }

    override fun extractSubstanceString(string: String): String? {
        return try {
            val wholeFile = JSONObject(string)
            val data = wholeFile.getJSONObject("data")
            val substances = data.getJSONArray("substances")
            substances.toString()
        } catch (e: Exception) {
            null
        }
    }

    private fun parseCategories(wholeFile: JSONObject): List<Category> {
        val jsonCategories = wholeFile.getJSONArray("categories")
        val categories: MutableList<Category> = mutableListOf()
        for (i in 0 until jsonCategories.length()) {
            val jsonCategory = jsonCategories.getOptionalJSONObject(i) ?: continue
            val newCategory = parseCategory(jsonCategory) ?: continue
            categories.add(newCategory)
        }
        return categories
    }

    private fun parseSubstances(wholeFile: JSONObject): List<Substance> {
        val jsonSubstances = wholeFile.getJSONArray("substances")
        val substances: MutableList<Substance> = mutableListOf()
        for (i in 0 until jsonSubstances.length()) {
            val jsonCategory = jsonSubstances.getOptionalJSONObject(i) ?: continue
            val newSubstance = parseSubstance(jsonCategory)
            substances.add(newSubstance)
        }
        return substances
    }

    private fun parseCategory(jsonCategory: JSONObject): Category? {
        val name = jsonCategory.getOptionalString("name") ?: return null
        val description = jsonCategory.getOptionalString("description") ?: return null
        val url = jsonCategory.getOptionalString("url")
        val colorDecimal = jsonCategory.getOptionalLong("color") ?: 4278876927
        val color = Color(colorDecimal)
        return Category(name, description, url, color)
    }

    private fun parseSubstance(jsonSubstance: JSONObject): Substance {
        val name = jsonSubstance.getString("name")
        val jsonCommonNames = jsonSubstance.getOptionalJSONArray("commonNames")
        val commonNames = parseCommonNames(jsonCommonNames, removeName = name)
        val url = jsonSubstance.getString("url")
        val isApproved = jsonSubstance.getOptionalBoolean("isApproved") ?: false
        val jsonTolerance = jsonSubstance.getOptionalJSONObject("tolerance")
        val tolerance = parseTolerance(jsonTolerance)
        val jsonTolerances = jsonSubstance.getOptionalJSONArray("crossTolerances")
        val crossTolerances = parseCrossTolerances(jsonTolerances)
        val addictionPotential = jsonSubstance.getOptionalString("addictionPotential")
        val jsonToxicities = jsonSubstance.getOptionalJSONArray("toxicities")
        val toxicities = parseJsonArrayToStringArray(jsonToxicities)
        val jsonCategory = jsonSubstance.getOptionalJSONArray("categories")
        val categories = parseJsonArrayToStringArray(jsonCategory)
        val summary = jsonSubstance.getOptionalString("summary")
        val effectsSummary = jsonSubstance.getOptionalString("effectsSummary")
        val dosageRemark = jsonSubstance.getOptionalString("dosageRemark")
        val generalRisks = jsonSubstance.getOptionalString("generalRisks")
        val longtermRisks = jsonSubstance.getOptionalString("longtermRisks")
        val jsonSaferUse = jsonSubstance.getOptionalJSONArray("saferUse")
        val saferUse = parseJsonArrayToStringArray(jsonSaferUse)
        val jsonInteractions = jsonSubstance.getOptionalJSONObject("interactions")
        val interactions = parseInteractions(jsonInteractions)
        val jsonRoas = jsonSubstance.getOptionalJSONArray("roas")
        val roas = parseRoas(jsonRoas)
        return Substance(
            name = name,
            commonNames = commonNames,
            url = url,
            isApproved = isApproved,
            tolerance = tolerance,
            crossTolerances = crossTolerances,
            addictionPotential = addictionPotential,
            toxicities = toxicities,
            categories = categories,
            summary = summary,
            effectsSummary = effectsSummary,
            dosageRemark = dosageRemark,
            generalRisks = generalRisks,
            longtermRisks = longtermRisks,
            saferUse = saferUse,
            interactions = interactions,
            roas = roas,
        )
    }

    private fun parseInteractions(jsonInteractions: JSONObject?): Interactions? {
        if (jsonInteractions == null) return null
        val jsonUncertain = jsonInteractions.getOptionalJSONArray("uncertain")
        val uncertainInteractions = parseJsonArrayToStringArray(jsonUncertain)
        val jsonUnsafe = jsonInteractions.getOptionalJSONArray("unsafe")
        val unsafeInteractions = parseJsonArrayToStringArray(jsonUnsafe)
        val jsonDangerous = jsonInteractions.getOptionalJSONArray("dangerous")
        val dangerousInteractions = parseJsonArrayToStringArray(jsonDangerous)
        return Interactions(
            dangerous = dangerousInteractions,
            unsafe = unsafeInteractions,
            uncertain = uncertainInteractions
        )
    }

    private fun parseCommonNames(jsonNames: JSONArray?, removeName: String): List<String> {
        if (jsonNames == null) return emptyList()
        val commonNames: MutableList<String> = mutableListOf()
        for (i in 0 until jsonNames.length()) {
            val commonName = jsonNames.getOptionalString(i) ?: continue
            if (commonName != removeName) {
                commonNames.add(commonName)
            }
        }
        return commonNames
    }

    private fun parseJsonArrayToStringArray(jsonArray: JSONArray?): List<String> {
        if (jsonArray == null) return emptyList()
        val result: MutableList<String> = mutableListOf()
        for (i in 0 until jsonArray.length()) {
            val item = jsonArray.getOptionalString(i) ?: continue
            result.add(item)
        }
        return result
    }

    private fun parseTolerance(jsonTolerance: JSONObject?): Tolerance? {
        if (jsonTolerance == null) return null
        val full = jsonTolerance.getOptionalString("full")
        val half = jsonTolerance.getOptionalString("half")
        val zero = jsonTolerance.getOptionalString("zero")
        return if (full == null && half == null && zero == null) {
            null
        } else {
            Tolerance(full, half, zero)
        }
    }

    private fun parseRoas(jsonRoas: JSONArray?): List<Roa> {
        if (jsonRoas == null) return emptyList()
        val roas: MutableList<Roa> = mutableListOf()
        for (i in 0 until jsonRoas.length()) {
            val oneJsonRoa = jsonRoas.getOptionalJSONObject(i) ?: continue
            val roa = parseRoa(oneJsonRoa)
            if (roa != null) {
                roas.add(roa)
            }
        }
        return roas
    }

    private fun parseRoa(oneJsonRoa: JSONObject): Roa? {
        val routeName = oneJsonRoa.getOptionalString("name")?.uppercase() ?: return null
        val route = when (routeName) {
            AdministrationRoute.ORAL.name -> AdministrationRoute.ORAL
            AdministrationRoute.SUBLINGUAL.name -> AdministrationRoute.SUBLINGUAL
            AdministrationRoute.BUCCAL.name -> AdministrationRoute.BUCCAL
            AdministrationRoute.INSUFFLATED.name -> AdministrationRoute.INSUFFLATED
            AdministrationRoute.RECTAL.name -> AdministrationRoute.RECTAL
            AdministrationRoute.TRANSDERMAL.name -> AdministrationRoute.TRANSDERMAL
            AdministrationRoute.SUBCUTANEOUS.name -> AdministrationRoute.SUBCUTANEOUS
            AdministrationRoute.INTRAMUSCULAR.name -> AdministrationRoute.INTRAMUSCULAR
            AdministrationRoute.INTRAVENOUS.name -> AdministrationRoute.INTRAVENOUS
            AdministrationRoute.SMOKED.name -> AdministrationRoute.SMOKED
            AdministrationRoute.INHALED.name -> AdministrationRoute.INHALED
            else -> return null
        }
        val jsonRoaDose = oneJsonRoa.getOptionalJSONObject("dose")
        val roaDose = parseRoaDose(jsonRoaDose)
        val jsonRoaDuration = oneJsonRoa.getOptionalJSONObject("duration")
        val roaDuration = parseRoaDuration(jsonRoaDuration)
        val jsonBio = oneJsonRoa.getOptionalJSONObject("bioavailability")
        val bioavailability = parseBioavailability(jsonBio)
        return Roa(
            route = route,
            roaDose = roaDose,
            roaDuration = roaDuration,
            bioavailability = bioavailability
        )
    }

    private fun parseRoaDose(jsonDose: JSONObject?): RoaDose? {
        if (jsonDose == null) return null
        val units = jsonDose.getOptionalString("units")
        val threshold = jsonDose.getOptionalDouble("threshold")
        val jsonLight = jsonDose.getOptionalJSONObject("light")
        val light = parseDoseRange(jsonLight)
        val jsonCommon = jsonDose.getOptionalJSONObject("common")
        val common = parseDoseRange(jsonCommon)
        val jsonStrong = jsonDose.getOptionalJSONObject("strong")
        val strong = parseDoseRange(jsonStrong)
        val heavy = jsonDose.getOptionalDouble("heavy")
        return if (units == null && threshold == null && light == null && common == null && strong == null && heavy == null) {
            null
        } else {
            RoaDose(
                units = units,
                threshold = threshold,
                light = light,
                common = common,
                strong = strong,
                heavy = heavy
            )
        }
    }

    private fun parseDoseRange(jsonRange: JSONObject?): DoseRange? {
        if (jsonRange == null) return null
        val min = jsonRange.getOptionalDouble("min")
        val max = jsonRange.getOptionalDouble("max")
        return if (min == null && max == null) {
            null
        } else {
            DoseRange(min, max)
        }
    }

    private fun parseRoaDuration(jsonRoaDuration: JSONObject?): RoaDuration? {
        if (jsonRoaDuration == null) return null
        val jsonOnset = jsonRoaDuration.getOptionalJSONObject("onset")
        val onset = parseDurationRange(jsonOnset)
        val jsonComeup = jsonRoaDuration.getOptionalJSONObject("comeup")
        val comeup = parseDurationRange(jsonComeup)
        val jsonPeak = jsonRoaDuration.getOptionalJSONObject("peak")
        val peak = parseDurationRange(jsonPeak)
        val jsonOffset = jsonRoaDuration.getOptionalJSONObject("offset")
        val offset = parseDurationRange(jsonOffset)
        val jsonTotal = jsonRoaDuration.getOptionalJSONObject("total")
        val total = parseDurationRange(jsonTotal)
        val jsonAfterglow = jsonRoaDuration.getOptionalJSONObject("afterglow")
        val afterglow = parseDurationRange(jsonAfterglow)
        return if (onset == null && comeup == null && peak == null && offset == null && total == null && afterglow == null) {
            null
        } else {
            RoaDuration(
                onset = onset,
                comeup = comeup,
                peak = peak,
                offset = offset,
                total = total,
                afterglow = afterglow
            )
        }
    }

    private fun parseDurationRange(jsonDurationRange: JSONObject?): DurationRange? {
        if (jsonDurationRange == null) return null
        val units = jsonDurationRange.getOptionalString("units")
        val min = jsonDurationRange.getOptionalDouble("min")
        val max = jsonDurationRange.getOptionalDouble("max")
        if ((min == null && max == null) || units == null) {
            return null
        }
        val durationUnits = when (units) {
            DurationUnits.SECONDS.text -> DurationUnits.SECONDS
            DurationUnits.MINUTES.text -> DurationUnits.MINUTES
            DurationUnits.HOURS.text -> DurationUnits.HOURS
            DurationUnits.DAYS.text -> DurationUnits.DAYS
            else -> null
        }
        return DurationRange(
            min?.toFloat(),
            max?.toFloat(),
            durationUnits
        )
    }

    private fun parseBioavailability(jsonBio: JSONObject?): Bioavailability? {
        if (jsonBio == null) return null
        val min = jsonBio.getOptionalDouble("min")
        val max = jsonBio.getOptionalDouble("max")
        return if (min == null && max == null) {
            null
        } else {
            Bioavailability(min, max)
        }
    }

    private fun parseCrossTolerances(jsonTolerances: JSONArray?): List<String> {
        if (jsonTolerances == null) return emptyList()
        val tolNames: MutableList<String> = mutableListOf()
        for (i in 0 until jsonTolerances.length()) {
            val tolName = jsonTolerances.getOptionalString(i) ?: continue
            tolNames.add(tolName)
        }
        return tolNames
    }
}