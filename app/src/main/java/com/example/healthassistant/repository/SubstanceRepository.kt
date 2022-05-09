package com.example.healthassistant.repository

import com.example.healthassistant.model.SubstanceModel

interface SubstanceRepository {
    suspend fun getSubstances(): List<SubstanceModel>
}