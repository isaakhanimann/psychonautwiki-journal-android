package com.isaakhanimann.journal.data.gamification

import kotlinx.coroutines.flow.StateFlow

interface GamificationService {
    val userLevel: StateFlow<UserLevel>
    val achievements: StateFlow<List<Achievement>>
    val streaks: StateFlow<List<Streak>>
    val safetyScore: StateFlow<SafetyScore?>

    suspend fun addXp(amount: Long)
    suspend fun unlockAchievement(achievementId: String)
    suspend fun updateStreak(type: StreakType)
    suspend fun calculateSafetyScore()
}