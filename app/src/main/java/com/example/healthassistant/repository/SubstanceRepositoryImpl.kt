package com.example.healthassistant.repository

import android.content.Context
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.data.substances.SubstanceParserInterface
import com.example.healthassistant.util.getSubstanceFileContent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val substanceParser: SubstanceParserInterface
): SubstanceRepository {

    override suspend fun getSubstances(): List<Substance> {
        val fileContent = getSubstanceFileContent(appContext)
        return substanceParser.parseAllSubstances(string = fileContent)
    }
}