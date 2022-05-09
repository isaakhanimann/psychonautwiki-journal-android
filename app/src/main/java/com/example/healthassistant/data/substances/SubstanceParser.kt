package com.example.healthassistant.data.substances

import com.example.healthassistant.model.SubstanceModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceParser @Inject constructor() : JSONParser<SubstanceModel> {

    override suspend fun parse(string: String): List<SubstanceModel> {
//        return Json.decodeFromString<List<SubstanceDecoded>>(string).map { it.toSubstanceModel() }
        return listOf(
            SubstanceModel("Cocaine", "https.apple.ch"),
            SubstanceModel("Heroin", "https.apple.ch")
        )

    }
}