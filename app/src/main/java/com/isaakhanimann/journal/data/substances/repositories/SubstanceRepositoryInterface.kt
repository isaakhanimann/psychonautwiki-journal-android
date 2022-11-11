/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.data.substances.repositories

import com.isaakhanimann.journal.data.substances.classes.Category
import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.classes.Substance
import com.isaakhanimann.journal.data.substances.classes.SubstanceWithCategories

interface SubstanceRepositoryInterface {
    fun getAllSubstances(): List<Substance>
    fun getAllSubstancesWithCategories(): List<SubstanceWithCategories>
    fun getAllCategories(): List<Category>
    fun getSubstance(substanceName: String): Substance?
    fun getCategory(categoryName: String): Category?
    fun getSubstanceWithCategories(substanceName: String): SubstanceWithCategories?
    suspend fun getAllInteractions(
        type: InteractionType,
        substanceName: String,
        originalInteractions: List<String>,
        interactionsToFilterOut: List<String>,
        categories: List<String>
    ): List<String>
}