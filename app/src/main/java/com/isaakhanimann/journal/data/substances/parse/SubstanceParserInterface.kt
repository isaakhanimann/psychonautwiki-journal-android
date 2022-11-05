package com.isaakhanimann.journal.data.substances.parse

import com.isaakhanimann.journal.data.substances.classes.SubstanceFile

interface SubstanceParserInterface {
    fun parseSubstanceFile(string: String): SubstanceFile
    fun extractSubstanceString(string: String): String?
}