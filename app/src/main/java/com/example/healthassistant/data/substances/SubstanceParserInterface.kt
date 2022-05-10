package com.example.healthassistant.data.substances

interface SubstanceParserInterface {
    fun parseAllSubstances(string: String): List<Substance>
}