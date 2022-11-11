/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

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