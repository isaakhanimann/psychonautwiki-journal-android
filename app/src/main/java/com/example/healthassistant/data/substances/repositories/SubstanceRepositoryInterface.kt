package com.example.healthassistant.data.substances.repositories

import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.Substance
import kotlinx.coroutines.flow.Flow

interface SubstanceRepositoryInterface {
    fun getAllSubstances(): Flow<List<Substance>>
    fun getSubstance(substanceName: String): Substance?
    fun getSubstances(searchText: String): Flow<List<Substance>>
    suspend fun getAllInteractions(
        type: InteractionType,
        substanceName: String,
        originalInteractions: List<String>,
        interactionsToFilterOut: List<String>,
        psychoactiveClassNames: List<String>
    ): List<String>
}