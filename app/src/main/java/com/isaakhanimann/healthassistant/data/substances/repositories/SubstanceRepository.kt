package com.isaakhanimann.healthassistant.data.substances.repositories

import android.content.Context
import com.isaakhanimann.healthassistant.data.DataStorePreferences
import com.isaakhanimann.healthassistant.data.substances.PsychonautWikiAPIImplementation
import com.isaakhanimann.healthassistant.data.substances.classes.*
import com.isaakhanimann.healthassistant.data.substances.parse.SubstanceParserInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val dataStorePreferences: DataStorePreferences,
    private val substanceParser: SubstanceParserInterface,
    private val psychonautWikiAPIImplementation: PsychonautWikiAPIImplementation
) : SubstanceRepositoryInterface {

    private var substanceFile: SubstanceFile

    private val fileFlow: MutableStateFlow<SubstanceFile>

    private val fetchedSubstancesFileName = "FetchedSubstances.json"
    private val fetchedFile = File(appContext.filesDir, fetchedSubstancesFileName)

    init {
        val fileContent = if (fetchedFile.exists()) {
            getFetchedSubstancesFileContent()
        } else {
            getAssetsSubstanceFileContent()
        }
        substanceFile = substanceParser.parseSubstanceFile(string = fileContent)
        fileFlow = MutableStateFlow(substanceFile)
    }

    suspend fun reset() {
        deleteFetchedSubstancesFile()
        dataStorePreferences.resetDate()
        loadSubstancesFromAsset()
    }

    private fun loadSubstancesFromAsset() {
        val fileContent = getAssetsSubstanceFileContent()
        substanceFile = substanceParser.parseSubstanceFile(string = fileContent)
        fileFlow.update { substanceFile }
    }

    suspend fun update(): Boolean {
        return false
        // todo: implement
//        val text = psychonautWikiAPIImplementation.getStringFromAPI()
//        val extract = substanceParser.extractSubstanceString(text) ?: return false
//        val parsedSubstances = substanceParser.parseSubstanceFile(string = extract)
//        val isSuccess = parsedSubstances.size > 150
//        return if (isSuccess) {
//            writeIntoFetchedFile(value = extract)
//            dataStorePreferences.saveDate(Date())
//            substanceFile = parsedSubstances
//            substancesFlow.update { substanceFile }
//            true
//        } else {
//            false
//        }
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
        return fileFlow.map { it.substances }
    }

    override fun getAllSubstancesWithCategoriesFlow(): Flow<List<SubstanceWithCategories>> {
        return fileFlow.map { file ->
            file.substances.map { substance ->
                SubstanceWithCategories(
                    substance = substance,
                    categories = file.categories.filter { category ->
                        substance.categories.contains(category.name)
                    }
                )
            }
        }
    }

    override fun getAllCategoriesFlow(): Flow<List<Category>> {
        return fileFlow.map { it.categories }
    }

    override fun getSubstance(substanceName: String): Substance? {
        return substanceFile.substances.firstOrNull { it.name == substanceName }
    }

    override fun getSubstanceWithCategories(substanceName: String): SubstanceWithCategories? {
        val substance =
            substanceFile.substances.firstOrNull { it.name == substanceName } ?: return null
        return SubstanceWithCategories(
            substance = substance,
            categories = substanceFile.categories.filter { substance.categories.contains(it.name) }
        )
    }

    override suspend fun getAllInteractions(
        type: InteractionType,
        substanceName: String,
        originalInteractions: List<String>,
        interactionsToFilterOut: List<String>,
        categories: List<String>
    ): List<String> {
        val otherInteractions = substanceFile.substances.filter { sub ->
            val interactions =
                sub.interactions?.getInteractions(interactionType = type) ?: emptyList()
            val isDirectMatch = interactions.contains(substanceName)
            val isClassMatch = categories.any { categoryName ->
                interactions.any { interactionName ->
                    interactionName.contains(categoryName, ignoreCase = true)
                }
            }
            isDirectMatch || isClassMatch
        }.map { it.name }
        val tooManyInteractions = (originalInteractions + otherInteractions).distinct()
        return tooManyInteractions.filter {
            !interactionsToFilterOut.contains(it)
        }
    }
}