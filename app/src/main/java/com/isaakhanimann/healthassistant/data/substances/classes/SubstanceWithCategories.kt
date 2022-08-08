package com.isaakhanimann.healthassistant.data.substances.classes

data class SubstanceWithCategories(
    val substance: Substance,
    val categories: List<Category>
)
