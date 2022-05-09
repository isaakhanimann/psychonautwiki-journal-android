package com.example.healthassistant.data.substances

import com.example.healthassistant.model.SubstanceModel
import org.json.JSONArray
import org.json.JSONTokener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubstanceParser @Inject constructor() : JSONParser<SubstanceModel> {

    override suspend fun parse(string: String): List<SubstanceModel> {
        val jsonArray = JSONTokener(string).nextValue() as JSONArray
        val substances: MutableList<SubstanceModel> = mutableListOf()
        for (i in 0 until jsonArray.length()) {
            val name = jsonArray.getJSONObject(i).getString("name")
            val url = jsonArray.getJSONObject(i).getString("url")
            val newSub = SubstanceModel(name, url)
            substances.add(newSub)
        }
        return substances
    }
}