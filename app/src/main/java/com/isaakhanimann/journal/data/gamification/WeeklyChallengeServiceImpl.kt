package com.isaakhanimann.journal.data.gamification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeeklyChallengeServiceImpl : WeeklyChallengeService {

    private val _weeklyChallenges = MutableStateFlow<List<WeeklyChallenge>>(emptyList())
    override val weeklyChallenges: StateFlow<List<WeeklyChallenge>> = _weeklyChallenges.asStateFlow()

    override suspend fun generateWeeklyChallenges() {
        val allChallenges = listOf(
            WeeklyChallenge(
                title = "Mindful Morning",
                description = "Start your day with a 5-minute meditation.",
                category = ChallengeCategory.MINDFULNESS,
                progress = 0,
                target = 5,
                xpReward = 50
            ),
            WeeklyChallenge(
                title = "Knowledge Boost",
                description = "Read one harm reduction article.",
                category = ChallengeCategory.KNOWLEDGE,
                progress = 0,
                target = 1,
                xpReward = 75
            ),
            WeeklyChallenge(
                title = "Safety First",
                description = "Document your safety precautions for one experience.",
                category = ChallengeCategory.SAFETY,
                progress = 0,
                target = 1,
                xpReward = 100
            )
        )
        _weeklyChallenges.value = allChallenges.shuffled().take(1)
    }

    override suspend fun updateChallengeProgress(challengeId: String, progress: Int) {
        val challenge = _weeklyChallenges.value.find { it.id == challengeId }
        if (challenge != null) {
            val updatedChallenges = _weeklyChallenges.value.map {
                if (it.id == challengeId) {
                    it.copy(progress = it.progress + progress)
                } else {
                    it
                }
            }
            _weeklyChallenges.value = updatedChallenges
        }
    }
}