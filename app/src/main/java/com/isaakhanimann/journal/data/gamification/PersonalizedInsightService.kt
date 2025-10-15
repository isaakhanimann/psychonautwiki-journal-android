package com.isaakhanimann.journal.data.gamification

import kotlinx.coroutines.flow.StateFlow

interface PersonalizedInsightService {
    val personalizedInsights: StateFlow<List<AIPersonalizedInsight>>

    suspend fun generatePersonalizedInsights()
}