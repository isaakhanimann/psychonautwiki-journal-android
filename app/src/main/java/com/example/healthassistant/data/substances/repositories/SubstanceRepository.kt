package com.example.healthassistant.data.substances.repositories

import android.content.Context
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.SubstanceParserInterface
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepository @Inject constructor(
    @ApplicationContext private val appContext: Context,
    substanceParser: SubstanceParserInterface
): SubstanceRepositoryInterface {

    private val allSubstances: List<Substance>

    init {
        val fileContent = getSubstanceFileContent()
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
    }

    private fun getSubstanceFileContent(): String {
        return appContext.assets.open("Substances-Date8May2022-Time15h12m.json").bufferedReader().use { it.readText() }
    }

    override fun getAllSubstances(): List<Substance> {
        return allSubstances
    }

    override fun getSubstance(substanceName: String): Substance? {
        return allSubstances.firstOrNull { it.name==substanceName }
    }

    override suspend fun getSubstances(searchText: String): List<Substance> {
        return if (searchText.isEmpty()) {
            allSubstances
        } else {
            allSubstances.filter {
                it.name.lowercase().contains(searchText.lowercase())
            }
        }
    }
}