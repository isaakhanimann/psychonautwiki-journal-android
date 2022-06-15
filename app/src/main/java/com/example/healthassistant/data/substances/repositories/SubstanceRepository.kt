package com.example.healthassistant.data.substances.repositories

import android.content.Context
import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.parse.SubstanceParserInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    substanceParser: SubstanceParserInterface
) : SubstanceRepositoryInterface {

    private val allSubstances: List<Substance>

    init {
        val fileContent = getSubstanceFileContent()
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
    }

    private fun getSubstanceFileContent(): String {
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