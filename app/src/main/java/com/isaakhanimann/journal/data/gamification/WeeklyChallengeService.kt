package com.isaakhanimann.journal.data.gamification

import kotlinx.coroutines.flow.StateFlow

interface WeeklyChallengeService {
    val weeklyChallenges: StateFlow<List<WeeklyChallenge>>

    suspend fun generateWeeklyChallenges()
    suspend fun updateChallengeProgress(challengeId: String, progress: Int)
}