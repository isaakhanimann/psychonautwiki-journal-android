package com.example.healthassistant.data.substances

interface SubstanceParserInterface {
    suspend fun parseAllSubstances(string: String): List<Substance>
}