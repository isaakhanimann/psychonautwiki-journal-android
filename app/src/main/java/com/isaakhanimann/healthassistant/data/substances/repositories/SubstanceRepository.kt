package com.isaakhanimann.healthassistant.data.substances.repositories

import android.content.Context
import com.isaakhanimann.healthassistant.data.DataStorePreferences
import com.isaakhanimann.healthassistant.data.substances.InteractionType
import com.isaakhanimann.healthassistant.data.substances.PsychonautWikiAPIImplementation
import com.isaakhanimann.healthassistant.data.substances.Substance
import com.isaakhanimann.healthassistant.data.substances.parse.SubstanceParserInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

    private val substancesFlow: MutableStateFlow<List<Substance>>

    private val fetchedSubstancesFileName = "FetchedSubstances.json"
    private val fetchedFile = File(appContext.filesDir, fetchedSubstancesFileName)

    init {
        val fileContent = if (fetchedFile.exists()) {
            getFetchedSubstancesFileContent()
        } else {
            getAssetsSubstanceFileContent()
        }
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
        substancesFlow = MutableStateFlow(allSubstances)
    }

    suspend fun reset() {
        deleteFetchedSubstancesFile()
        dataStorePreferences.resetDate()
        loadSubstancesFromAsset()
    }

    private fun loadSubstancesFromAsset() {
        val fileContent = getAssetsSubstanceFileContent()
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
        substancesFlow.update { allSubstances }
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
            substancesFlow.update { allSubstances }
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

    override fun getAllSubstances(): Flow<List<Substance>> {
        return substancesFlow
    }

    override fun getSubstance(substanceName: String): Substance? {
        return allSubstances.firstOrNull { it.name == substanceName }
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
            val isClassMatch = psychoactiveClassNames.any {
                interactions.contains(it)
            }
            isDirectMatch || isClassMatch
        }.map { it.name }
        val tooManyInteractions = (originalInteractions + otherInteractions).distinct()
        return tooManyInteractions.filter {
            !interactionsToFilterOut.contains(it)
        }
    }
}