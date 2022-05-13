package com.example.healthassistant.data.substances

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Singleton
class SubstanceParser @Inject constructor() : SubstanceParserInterface {

    override fun parseAllSubstances(string: String): List<Substance> {
        val jsonArray = JSONTokener(string).nextValue() as JSONArray
        val substances: MutableList<Substance> = mutableListOf()
        for (i in 0 until jsonArray.length()) {
            val jsonSubstance = jsonArray.getJSONObject(i)
            val newSub = parseSubstance(jsonSubstance)
            substances.add(newSub)
        }
        return substances
    }

    private fun parseSubstance(jsonSubstance: JSONObject): Substance {
        val name = jsonSubstance.getString("name")
        val jsonCommonNames = jsonSubstance.getOptionalJSONArray("commonNames")
        val commonNames = parseCommonNames(jsonCommonNames, removeName = name)
        val url = jsonSubstance.getString("url")
        val jsonEffects = jsonSubstance.getJSONArray("effects")
        val effects = parseEffects(jsonEffects)
        val jsonClass = jsonSubstance.getOptionalJSONObject("class")
        val chemicalClasses = parseChemicalClasses(jsonClass)
        val psychoactiveClasses = parsePsychoactiveClasses(jsonClass)
        val jsonTolerance = jsonSubstance.getOptionalJSONObject("tolerance")
        val tolerance = parseTolerance(jsonTolerance)
        val jsonRoas = jsonSubstance.getJSONArray("roas")
        val roas = parseRoas(jsonRoas)
        val addictionPotential = jsonSubstance.getOptionalString("addictionPotential")
        val toxicity = jsonSubstance.getOptionalJSONArray("toxicity")?.getOptionalString(0)
        val jsonTols = jsonSubstance.getOptionalJSONArray("crossTolerances")
        val crossTolerances = parseCrossTolerances(jsonTols)
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
            val commonName = jsonNames.getString(i)
            if (commonName != removeName) {
                commonNames.add(commonName)
            }
        }
        return commonNames
    }

    private fun parseEffects(jsonEffects: JSONArray): List<Effect> {
        val effects: MutableList<Effect> = mutableListOf()
        for (i in 0 until jsonEffects.length()) {
            val effectJson = jsonEffects.getJSONObject(i)
            val name = effectJson.getString("name")
            val url = effectJson.getString("url")
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
            val chemicalName = jsonChemicals.getString(i)
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
            val psyName = jsonPsychoactives.getString(i)
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

    private fun parseRoas(jsonRoas: JSONArray): List<Roa> {
        val roas: MutableList<Roa> = mutableListOf()
        for (i in 0 until jsonRoas.length()) {
            val oneJsonRoa = jsonRoas.getJSONObject(i)
            val roa = parseRoa(oneJsonRoa)
            roas.add(roa)
        }
        return roas
    }

    private fun parseRoa(oneJsonRoa: JSONObject): Roa {
        val name = oneJsonRoa.getString("name").uppercase()
        val route = AdministrationRoute.valueOf(name)
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
        if (min == null && max == null) {
            return null
        }
        when (units) {
            "seconds" -> return DurationRange(
                min = min?.toDuration(DurationUnit.SECONDS),
                max = max?.toDuration(DurationUnit.SECONDS)
            )
            "minutes" -> return DurationRange(
                min = min?.toDuration(DurationUnit.MINUTES),
                max = max?.toDuration(DurationUnit.MINUTES)
            )
            "hours" -> return DurationRange(
                min = min?.toDuration(DurationUnit.HOURS),
                max = max?.toDuration(DurationUnit.HOURS)
            )
            "days" -> return DurationRange(
                min = min?.toDuration(DurationUnit.DAYS),
                max = max?.toDuration(DurationUnit.DAYS)
            )
            else -> return null
        }
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

    private fun parseCrossTolerances(jsonTols: JSONArray?): List<String> {
        if (jsonTols == null) return emptyList()
        val tolNames: MutableList<String> = mutableListOf()
        for (i in 0 until jsonTols.length()) {
            val tolName = jsonTols.getString(i)
            tolNames.add(tolName)
        }
        return tolNames
    }

    private fun parseInteractions(jsonInteractions: JSONArray?): List<String> {
        if (jsonInteractions == null) return emptyList()
        val interactionNames: MutableList<String> = mutableListOf()
        for (i in 0 until jsonInteractions.length()) {
            val jsonInt = jsonInteractions.getJSONObject(i)
            val intName = jsonInt.getString("name")
            interactionNames.add(intName)
        }
        return interactionNames
    }
}