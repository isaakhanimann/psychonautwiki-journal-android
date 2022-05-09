package com.example.healthassistant.data.substances

import com.example.healthassistant.model.SubstanceModel

fun SubstanceDecoded.toSubstanceModel(): SubstanceModel {
    return SubstanceModel(name = name, url = url)
}