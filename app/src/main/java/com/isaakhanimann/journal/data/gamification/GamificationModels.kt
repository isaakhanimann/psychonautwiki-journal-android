package com.isaakhanimann.journal.data.gamification

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class UserLevel(
    val level: Int = 1,
    val xp: Long = 0,
    val nextLevelXp: Long = 100
)

@Serializable
data class Achievement(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val category: AchievementCategory,
    val tier: Int,
    val requirements: Map<String, Int>,
    val isUnlocked: Boolean = false,
    @Contextual
    val unlockedAt: Instant? = null
)

enum class AchievementCategory {
    SAFETY,
    KNOWLEDGE,
    CONSISTENCY,
    INTEGRATION,
    COMMUNITY_CARE,
    HARM_REDUCTION,
    MILESTONES
}

@Serializable
data class Streak(
    val type: StreakType,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    @Contextual
    val lastContributionDate: Instant? = null
)

enum class StreakType {
    DAILY_LOGGING,
    SAFETY_PRACTICE,
    INTEGRATION
}

@Serializable
data class SafetyScore(
    val score: Double,
    val trend: Double,
    @Contextual
    val lastCalculated: Instant
)

@Serializable
data class KnowledgeQuest(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: QuestCategory,
    val difficulty: QuestDifficulty,
    val xpReward: Int,
    val isCompleted: Boolean = false,
    @Contextual
    val completedAt: Instant? = null
)

enum class QuestCategory {
    SAFETY_PRACTICES,
    SUBSTANCE_KNOWLEDGE,
    INTEGRATION_TECHNIQUES
}

enum class QuestDifficulty {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}

@Serializable
data class WeeklyChallenge(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val category: ChallengeCategory,
    val progress: Int,
    val target: Int,
    val xpReward: Int,
    val isActive: Boolean = true
)

enum class ChallengeCategory {
    SAFETY,
    KNOWLEDGE,
    MINDFULNESS,
    DOCUMENTATION,
    COMMUNITY
}

@Serializable
data class AIPersonalizedInsight(
    val id: String = UUID.randomUUID().toString(),
    val insightType: InsightType,
    val content: String,
    @Contextual
    val generatedAt: Instant,
    val isActionable: Boolean
)

enum class InsightType {
    PATTERN_RECOGNITION,
    PERSONALIZED_RECOMMENDATION,
    PROGRESS_FEEDBACK,
    ACTIONABLE_STEPS,
    SAFETY_REMINDER,
    INTEGRATION_PROMPT
}