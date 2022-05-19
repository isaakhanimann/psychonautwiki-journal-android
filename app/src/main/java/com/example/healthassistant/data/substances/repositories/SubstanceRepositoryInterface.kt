package com.example.healthassistant.data.substances.repositories

import com.example.healthassistant.data.substances.Substance

interface SubstanceRepositoryInterface {
    fun getAllSubstances(): List<Substance>
    fun getSubstance(substanceName: String): Substance?
    suspend fun getSubstances(searchText: String): List<Substance>
}