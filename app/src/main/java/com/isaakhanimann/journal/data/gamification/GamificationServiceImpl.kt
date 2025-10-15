package com.isaakhanimann.journal.data.gamification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.isaakhanimann.journal.data.room.experiences.ExperienceDao
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import javax.inject.Inject

class GamificationServiceImpl @Inject constructor(
    private val experienceDao: ExperienceDao
) : GamificationService {

    private val _userLevel = MutableStateFlow(UserLevel())
    override val userLevel: StateFlow<UserLevel> = _userLevel.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    override val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    private val _streaks = MutableStateFlow<List<Streak>>(emptyList())
    override val streaks: StateFlow<List<Streak>> = _streaks.asStateFlow()

    private val _safetyScore = MutableStateFlow<SafetyScore?>(null)
    override val safetyScore: StateFlow<SafetyScore?> = _safetyScore.asStateFlow()

    override suspend fun addXp(amount: Long) {
        val currentLevel = _userLevel.value
        val newXp = currentLevel.xp + amount
        if (newXp >= currentLevel.nextLevelXp) {
            _userLevel.value = currentLevel.copy(
                level = currentLevel.level + 1,
                xp = newXp - currentLevel.nextLevelXp,
                nextLevelXp = (currentLevel.nextLevelXp * 1.5).toLong()
            )
        } else {
            _userLevel.value = currentLevel.copy(xp = newXp)
        }
    }

    override suspend fun unlockAchievement(achievementId: String) {
        val achievement = _achievements.value.find { it.id == achievementId }
        if (achievement != null && !achievement.isUnlocked) {
            val updatedAchievements = _achievements.value.map {
                if (it.id == achievementId) {
                    it.copy(isUnlocked = true, unlockedAt = Instant.now())
                } else {
                    it
                }
            }
            _achievements.value = updatedAchievements
        }
    }

    override suspend fun updateStreak(type: StreakType) {
        val streak = _streaks.value.find { it.type == type }
        if (streak != null) {
            val updatedStreaks = _streaks.value.map {
                if (it.type == type) {
                    it.copy(
                        currentStreak = it.currentStreak + 1,
                        longestStreak = maxOf(it.currentStreak + 1, it.longestStreak),
                        lastContributionDate = Instant.now()
                    )
                } else {
                    it
                }
            }
            _streaks.value = updatedStreaks
        } else {
            val newStreak = Streak(type = type, currentStreak = 1, longestStreak = 1, lastContributionDate = Instant.now())
            _streaks.value = _streaks.value + newStreak
        }
    }

    override suspend fun calculateSafetyScore() {
        val experiences = experienceDao.getAllExperiencesWithIngestionsTimedNotesAndRatingsSorted()
        if (experiences.isEmpty()) {
            _safetyScore.value = null
            return
        }

        var totalScore = 0.0
        var scoreCount = 0
        val scores = mutableListOf<Double>()

        for (experience in experiences) {
            for (ingestion in experience.ingestions) {
                var score = 100.0
                // Penalize for risky ROAs
                when (ingestion.administrationRoute) {
                    com.isaakhanimann.journal.data.substances.AdministrationRoute.INTRAVENOUS -> score -= 50
                    com.isaakhanimann.journal.data.substances.AdministrationRoute.INTRAMUSCULAR -> score -= 40
                    com.isaakhanimann.journal.data.substances.AdministrationRoute.SMOKED -> score -= 30
                    com.isaakhanimann.journal.data.substances.AdministrationRoute.INHALED -> score -= 20
                    else -> Unit
                }
                // Penalize for risky substances (this is a simplified example)
                if (ingestion.substanceName.contains("fentanyl", ignoreCase = true)) {
                    score -= 50
                }
                totalScore += score
                scoreCount++
                scores.add(score)
            }
        }

        val averageScore = if (scoreCount > 0) totalScore / scoreCount else 0.0
        val trend = if (scores.size > 1) scores.last() - scores.first() else 0.0

        _safetyScore.value = SafetyScore(score = averageScore, trend = trend, lastCalculated = Instant.now())
    }
}