package com.example.healthassistant

import com.example.healthassistant.data.substances.parse.SubstanceParser
import org.junit.Assert.assertTrue
import org.junit.Test

class TestParse {
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