package com.example.healthassistant.presentation.substances

import com.example.healthassistant.model.SubstanceModel

data class AllSubstancesState(
    val substances: List<SubstanceModel> = emptyList(),
    val error: String? = null
)