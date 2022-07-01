package com.example.healthassistant.data.substances.parse

import com.example.healthassistant.data.substances.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceParser @Inject constructor() : SubstanceParserInterface {

    override fun parseAllSubstances(string: String): List<Substance> {
        val jsonArray = JSONTokener(string).nextValue() as? JSONArray ?: return emptyList()
        val substances: MutableList<Substance> = mutableListOf()
        for (i in 0 until jsonArray.length()) {
            val jsonSubstance = jsonArray.getOptionalJSONObject(i) ?: continue
            val newSub = parseSubstance(jsonSubstance) ?: continue
            substances.add(newSub)
        }
        return substances
    }

    override fun extractSubstanceString(string: String): String? {
        val wholeFile = try {
            JSONObject(string)
        } catch (e: Exception) {
            return null
        }
        val data = wholeFile.getOptionalJSONObject("data")
        val substances = data?.getOptionalJSONArray("substances")
        return substances?.toString()
    }

    private fun parseSubstance(jsonSubstance: JSONObject): Substance? {
        val name = jsonSubstance.getOptionalString("name") ?: return null
        val jsonCommonNames = jsonSubstance.getOptionalJSONArray("commonNames")
        val commonNames = parseCommonNames(jsonCommonNames, removeName = name)
        val url = jsonSubstance.getOptionalString("url") ?: return null
        val jsonEffects = jsonSubstance.getOptionalJSONArray("effects")
        val effects = parseEffects(jsonEffects)
        val jsonClass = jsonSubstance.getOptionalJSONObject("class")
        val chemicalClasses = parseChemicalClasses(jsonClass)
        val psychoactiveClasses = parsePsychoactiveClasses(jsonClass)
        val jsonTolerance = jsonSubstance.getOptionalJSONObject("tolerance")
        val tolerance = parseTolerance(jsonTolerance)
        val jsonRoas = jsonSubstance.getOptionalJSONArray("roas")
        val roas = parseRoas(jsonRoas)
        val addictionPotential = jsonSubstance.getOptionalString("addictionPotential")
        val toxicity = jsonSubstance.getOptionalJSONArray("toxicity")?.getOptionalString(0)
        val jsonTolerances = jsonSubstance.getOptionalJSONArray("crossTolerances")
        val crossTolerances = parseCrossTolerances(jsonTolerances)
        val jsonUncertain = jsonSubstance.getOptionalJSONArray("uncertainInteractions")
        val uncertainInteractions = parseInteractions(jsonUncertain)
        val jsonUnsafe = jsonSubstance.getOptionalJSONArray("unsafeInteractions")
        val unsafeInteractions = parseInteractions(jsonUnsafe)
        val jsonDangerous = jsonSubstance.getOptionalJSONArray("dangerousInteractions")
        val dangerousInteractions = parseInteractions(jsonDangerous)
        return Substance(
            name = name,
            commonNames = commonNames,
            url = url,
            effects = effects,
            chemicalClasses = chemicalClasses,
            psychoactiveClasses = psychoactiveClasses,
            tolerance = tolerance,
            roas = roas,
            addictionPotential = addictionPotential,
            toxicity = toxicity,
            crossTolerances = crossTolerances,
            uncertainInteractions = uncertainInteractions,
            unsafeInteractions = unsafeInteractions,
            dangerousInteractions = dangerousInteractions
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

    private fun parseEffects(jsonEffects: JSONArray?): List<Effect> {
        if (jsonEffects == null) return emptyList()
        val effects: MutableList<Effect> = mutableListOf()
        for (i in 0 until jsonEffects.length()) {
            val effectJson = jsonEffects.getOptionalJSONObject(i) ?: continue
            val name = effectJson.getOptionalString("name") ?: continue
            val url = effectJson.getOptionalString("url") ?: continue
            val newEffect = Effect(name, url)
            effects.add(newEffect)
        }
        return effects
    }

    private fun parseChemicalClasses(jsonClass: JSONObject?): List<String> {
        if (jsonClass == null) return emptyList()
        val jsonChemicals = jsonClass.getOptionalJSONArray("chemical")
        val chemicals: MutableList<String> = mutableListOf()
        if (jsonChemicals == null) return chemicals
        for (i in 0 until jsonChemicals.length()) {
            val chemicalName = jsonChemicals.getOptionalString(i) ?: continue
            chemicals.add(chemicalName)
        }
        return chemicals
    }

    private fun parsePsychoactiveClasses(jsonClass: JSONObject?): List<String> {
        if (jsonClass == null) return emptyList()
        val jsonPsychoactives = jsonClass.getOptionalJSONArray("psychoactive")
        val psychoactives: MutableList<String> = mutableListOf()
        if (jsonPsychoactives == null) return psychoactives
        for (i in 0 until jsonPsychoactives.length()) {
            val psyName = jsonPsychoactives.getOptionalString(i) ?: continue
            psychoactives.add(psyName)
        }
        return psychoactives
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
            AdministrationRoute.SUBLINGUAL.name -> AdministrationRoute.BUCCAL
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

    private fun parseInteractions(jsonInteractions: JSONArray?): List<String> {
        if (jsonInteractions == null) return emptyList()
        val interactionNames: MutableList<String> = mutableListOf()
        for (i in 0 until jsonInteractions.length()) {
            val jsonInt = jsonInteractions.getOptionalJSONObject(i) ?: continue
            val intName = jsonInt.getOptionalString("name") ?: continue
            interactionNames.add(intName)
        }
        return interactionNames
    }
}