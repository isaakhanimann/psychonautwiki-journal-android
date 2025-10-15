package com.isaakhanimann.journal.data.gamification

import com.isaakhanimann.journal.data.room.experiences.ExperienceDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.Instant
import javax.inject.Inject

class PersonalizedInsightServiceImpl @Inject constructor(
    private val experienceDao: ExperienceDao
) : PersonalizedInsightService {

    private val _personalizedInsights = MutableStateFlow<List<AIPersonalizedInsight>>(emptyList())
    override val personalizedInsights: StateFlow<List<AIPersonalizedInsight>> = _personalizedInsights.asStateFlow()

    override suspend fun generatePersonalizedInsights() {
        val allInsights = mutableListOf<AIPersonalizedInsight>()

        // Add a generic insight
        allInsights.add(
            AIPersonalizedInsight(
                insightType = InsightType.PROGRESS_FEEDBACK,
                content = "You've been consistently journaling. Keep it up!",
                generatedAt = Instant.now(),
                isActionable = false
            )
        )

        // Add an insight based on the last experience
        val lastExperience = experienceDao.getAllExperiencesWithIngestionsTimedNotesAndRatingsSorted().lastOrNull()
        if (lastExperience != null) {
            allInsights.add(
                AIPersonalizedInsight(
                    insightType = InsightType.INTEGRATION_PROMPT,
                    content = "How are you integrating the lessons from your experience with '${lastExperience.experience.title}'?",
                    generatedAt = Instant.now(),
                    isActionable = true
                )
            )
        }

        _personalizedInsights.value = allInsights.shuffled().take(1)
    }
}