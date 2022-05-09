package com.example.healthassistant.repository

import android.content.Context
import com.example.healthassistant.data.substances.JSONParser
import com.example.healthassistant.model.SubstanceModel
import com.example.healthassistant.util.getSubstanceFileContent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceRepositoryImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val substanceParser: JSONParser<SubstanceModel>
): SubstanceRepository {

    override suspend fun getSubstances(): List<SubstanceModel> {
        val fileContent = getSubstanceFileContent(appContext)
        return substanceParser.parse(string = fileContent)
    }
}