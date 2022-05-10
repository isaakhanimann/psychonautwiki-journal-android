package com.example.healthassistant.repository

import android.content.Context
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.SubstanceParserInterface
import com.example.healthassistant.util.getSubstanceFileContent
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
        val fileContent = getSubstanceFileContent(appContext)
        allSubstances = substanceParser.parseAllSubstances(string = fileContent)
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