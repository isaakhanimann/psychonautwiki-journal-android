package com.isaakhanimann.healthassistant.data.substances.repositories

import android.content.Context
import com.isaakhanimann.healthassistant.data.substances.classes.*
import com.isaakhanimann.healthassistant.data.substances.parse.SubstanceParserInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    substanceParser: SubstanceParserInterface,
) : SubstanceRepositoryInterface {

    private var substanceFile: SubstanceFile

    init {
        val fileContent = getAssetsSubstanceFileContent()
        substanceFile = substanceParser.parseSubstanceFile(string = fileContent)
    }

    private fun getAssetsSubstanceFileContent(): String {
        return appContext.assets.open("Substances.json").bufferedReader().use { it.readText() }
    }

    override fun getAllSubstances(): List<Substance> {
        return substanceFile.substances
    }

    override fun getAllSubstancesWithCategories(): List<SubstanceWithCategories> {
        return substanceFile.substances.map { substance ->
            SubstanceWithCategories(
                substance = substance,
                categories = substanceFile.categories.filter { category ->
                    substance.categories.contains(category.name)
                }
            )
        }
    }

    override fun getAllCategories(): List<Category> {
        return substanceFile.categories
    }

    override fun getSubstance(substanceName: String): Substance? {
        return substanceFile.substancesMap[substanceName]
    }

    override fun getCategory(categoryName: String): Category? {
        return substanceFile.categories.firstOrNull { it.name == categoryName }
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