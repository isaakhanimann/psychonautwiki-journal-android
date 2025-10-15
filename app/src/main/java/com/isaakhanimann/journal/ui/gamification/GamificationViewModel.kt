package com.isaakhanimann.journal.ui.gamification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.gamification.AIPersonalizedInsight
import com.isaakhanimann.journal.data.gamification.Achievement
import com.isaakhanimann.journal.data.gamification.GamificationService
import com.isaakhanimann.journal.data.gamification.KnowledgeQuest
import com.isaakhanimann.journal.data.gamification.PersonalizedInsightService
import com.isaakhanimann.journal.data.gamification.Streak
import com.isaakhanimann.journal.data.gamification.UserLevel
import com.isaakhanimann.journal.data.gamification.WeeklyChallenge
import com.isaakhanimann.journal.data.gamification.WeeklyChallengeService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GamificationScreenState(
    val userLevel: UserLevel = UserLevel(),
    val achievements: List<Achievement> = emptyList(),
    val streaks: List<Streak> = emptyList(),
    val knowledgeQuests: List<KnowledgeQuest> = emptyList(),
    val weeklyChallenges: List<WeeklyChallenge> = emptyList(),
    val personalizedInsights: List<AIPersonalizedInsight> = emptyList()
)

@HiltViewModel
class GamificationViewModel @Inject constructor(
    private val gamificationService: GamificationService,
    private val weeklyChallengeService: WeeklyChallengeService,
    private val personalizedInsightService: PersonalizedInsightService
    // TODO: Add KnowledgeQuestService
) : ViewModel() {

    val state: StateFlow<GamificationScreenState> = combine(
        gamificationService.userLevel,
        gamificationService.achievements,
        gamificationService.streaks,
        weeklyChallengeService.weeklyChallenges,
        personalizedInsightService.personalizedInsights
    ) { level, achievements, streaks, challenges, insights ->
        GamificationScreenState(
            userLevel = level,
            achievements = achievements,
            streaks = streaks,
            weeklyChallenges = challenges,
            personalizedInsights = insights
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GamificationScreenState()
    )

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            weeklyChallengeService.generateWeeklyChallenges()
            personalizedInsightService.generatePersonalizedInsights()
            // TODO: Load achievements, quests, etc. from services
        }
    }
}