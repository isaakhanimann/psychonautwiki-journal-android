package com.isaakhanimann.journal.data.gamification

import com.isaakhanimann.journal.data.JournalPlugin

class GamificationPlugin(
    private val gamificationService: GamificationService,
    private val weeklyChallengeService: WeeklyChallengeService,
    private val personalizedInsightService: PersonalizedInsightService
) : JournalPlugin {
    override val name: String = "Gamification"

    fun onAppStart() {
        // Initialize services
    }
}