package com.isaakhanimann.journal.data.substances.classes

class SubstanceFile(
    categories: List<Category>,
    substances: List<Substance>
) {
    val categories: List<Category>
    val substances: List<Substance>
    val substancesMap: Map<String, Substance>

    init {
        this.categories = categories
        this.substances = substances
        this.substancesMap = substances.map { it.name to it }.toMap()
    }
}