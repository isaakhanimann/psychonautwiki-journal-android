package com.example.healthassistant.repository

import com.example.healthassistant.data.substances.Substance

interface SubstanceRepository {
    suspend fun getSubstances(): List<Substance>
}