package com.example.healthassistant.data.substances.repositories

import android.content.Context
import com.example.healthassistant.data.DataStorePreferences
import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.PsychonautWikiAPIImplementation
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.parse.SubstanceParserInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val dataStorePreferences: DataStorePreferences,
    private val substanceParser: SubstanceParserInterface,
    private val psychonautWikiAPIImplementation: PsychonautWikiAPIImplementation
) : SubstanceRepositoryInterface {

    private var allSubstances: List<Substance>

    private val fetchedSubstancesFileName = "FetchedSubstances.json"
    private val fetchedFile = File(appContext.filesDir, fetchedSubstancesFileName)

    init {
        val fileContent = if (fetchedFile.exists()) {
            getFetchedSubstancesFileContent()
        } else {
            getAssetsSubstanceFileContent()
        }
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
    }

    suspend fun reset() {
        deleteFetchedSubstancesFile()
        dataStorePreferences.resetDate()
        loadSubstancesFromAsset()
    }

    private fun loadSubstancesFromAsset() {
        val fileContent = getAssetsSubstanceFileContent()
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
    }

    suspend fun update(): Boolean {
        val text = psychonautWikiAPIImplementation.getStringFromAPI()
        val extract = substanceParser.extractSubstanceString(text) ?: return false
        val parsedSubstances = substanceParser.parseAllSubstances(string = extract)
        val isSuccess = parsedSubstances.size > 150
        return if (isSuccess) {
            writeIntoFetchedFile(value = extract)
            dataStorePreferences.saveDate(Date())
            allSubstances = parsedSubstances
            true
        } else {
            false
        }
    }

    private fun getFetchedSubstancesFileContent(): String {
        return fetchedFile.bufferedReader().use { it.readText() }
    }

    private fun deleteFetchedSubstancesFile() {
        fetchedFile.delete()
    }

    private fun writeIntoFetchedFile(value: String) {
        fetchedFile.writeText(value)
    }

    private fun getAssetsSubstanceFileContent(): String {
        return appContext.assets.open("Substances.json").bufferedReader().use { it.readText() }
    }

    override fun getAllSubstances(): List<Substance> {
        return allSubstances
    }

    override fun getSubstance(substanceName: String): Substance? {
        return allSubstances.firstOrNull { it.name == substanceName }
    }

    override suspend fun getSubstances(searchText: String): List<Substance> {
        return if (searchText.isEmpty()) {
            allSubstances
        } else {
            allSubstances.filter {
                it.name.startsWith(prefix = searchText, ignoreCase = true) ||
                        it.commonNames.any { commonName ->
                            commonName.startsWith(prefix = searchText, ignoreCase = true)
                        }
            }
        }
    }

    override suspend fun getAllInteractions(
        type: InteractionType,
        substanceName: String,
        originalInteractions: List<String>,
        interactionsToFilterOut: List<String>,
        psychoactiveClassNames: List<String>
    ): List<String> {
        val otherInteractions = allSubstances.filter { sub ->
            val interactions = sub.getInteractions(interactionType = type)
            val isDirectMatch = interactions.contains(substanceName)
            val isWildCardMatch = interactions.map { dangName ->
                Regex(
                    pattern = dangName.replace(
                        oldValue = "x",
                        newValue = "[\\S]*",
                        ignoreCase = true
                    ),
                    option = RegexOption.IGNORE_CASE
                ).matches(substanceName)
            }.any { it }
            val isClassMatch = psychoactiveClassNames.any {
                interactions.contains(it)
            }
            isDirectMatch || isWildCardMatch || isClassMatch
        }.map { it.name }
        val tooManyInteractions = originalInteractions + otherInteractions
        return tooManyInteractions.filter {
            !interactionsToFilterOut.contains(it)
        }
    }
}