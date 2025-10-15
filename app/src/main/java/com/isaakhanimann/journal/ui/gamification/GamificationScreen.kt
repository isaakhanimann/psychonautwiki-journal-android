package com.isaakhanimann.journal.ui.gamification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamificationScreen(
    viewModel: GamificationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gamification") })
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                UserLevelProgress(userLevel = state.userLevel)
            }

            item {
                Column {
                    Text("Achievements")
                    state.achievements.forEach { achievement ->
                        AchievementCard(achievement = achievement)
                    }
                }
            }

            item {
                Column {
                    Text("Quests")
                    state.knowledgeQuests.forEach { quest ->
                        QuestCard(quest = quest)
                    }
                }
            }

            item {
                Column {
                    Text("Weekly Challenges")
                    state.weeklyChallenges.forEach { challenge ->
                        ChallengeCard(challenge = challenge)
                    }
                }
            }

            items(state.personalizedInsights) { insight ->
                InsightCard(insight = insight)
            }
        }
    }
}