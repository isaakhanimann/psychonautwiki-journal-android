package com.example.healthassistant.data.substances.repositories

import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.Substance

interface SubstanceRepositoryInterface {
    fun getAllSubstances(): List<Substance>
    fun getSubstance(substanceName: String): Substance?
    suspend fun getSubstances(searchText: String): List<Substance>
    suspend fun getAllInteractions(
        type: InteractionType,
        substanceName: String,
        originalInteractions: List<String>,
        interactionsToFilterOut: List<String>,
        psychoactiveClassNames: List<String>
    ): List<String>
}