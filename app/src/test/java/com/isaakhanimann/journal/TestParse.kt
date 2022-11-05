package com.isaakhanimann.journal

import com.isaakhanimann.journal.data.substances.parse.SubstanceParser
import org.junit.Assert.assertTrue
import org.junit.Test

class TestParse {

    @Test
    fun noCrash() {
        val substances = SubstanceParser().parseSubstanceFile(string = "error")
        assertTrue(substances.substances.isEmpty())
    }

    @Test
    fun noCrashExtract() {
        val result = SubstanceParser().extractSubstanceString(string = "error")
        assertTrue(result == null)
    }

    @Test
    fun testExtractSubstancesString() {
        val text = """
{
  "data": {
    "substances": [
      {
        "name": "Armodafinil",
        "roas": [
          {
            "name": "oral"
          }
        ]
      }
    ]
  }
}"""
        val result = SubstanceParser().extractSubstanceString(string = text)
        assertTrue(result == "[{\"name\":\"Armodafinil\",\"roas\":[{\"name\":\"oral\"}]}]")
    }
}