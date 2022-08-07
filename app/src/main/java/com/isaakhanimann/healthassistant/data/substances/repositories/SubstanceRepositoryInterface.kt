package com.isaakhanimann.healthassistant.data.substances.repositories

import com.isaakhanimann.healthassistant.data.substances.classes.Category
import com.isaakhanimann.healthassistant.data.substances.classes.InteractionType
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import kotlinx.coroutines.flow.Flow

interface SubstanceRepositoryInterface {
    fun getAllSubstances(): Flow<List<Substance>>
    fun getAllCategoriesFlow(): Flow<List<Category>>
    fun getSubstance(substanceName: String): Substance?
    suspend fun getAllInteractions(
        type: InteractionType,
        substanceName: String,
        originalInteractions: List<String>,
        interactionsToFilterOut: List<String>,
        categories: List<String>
    ): List<String>
}