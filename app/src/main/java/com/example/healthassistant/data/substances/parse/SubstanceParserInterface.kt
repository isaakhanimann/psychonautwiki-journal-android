package com.example.healthassistant.data.substances.parse

import com.example.healthassistant.data.substances.Substance

interface SubstanceParserInterface {
    fun parseAllSubstances(string: String): List<Substance>
    fun extractSubstanceString(string: String): String?
}