package com.isaakhanimann.healthassistant.data.substances.parse

import com.isaakhanimann.healthassistant.data.substances.SubstanceFile

interface SubstanceParserInterface {
    fun parseSubstanceFile(string: String): SubstanceFile
    fun extractSubstanceString(string: String): String?
}