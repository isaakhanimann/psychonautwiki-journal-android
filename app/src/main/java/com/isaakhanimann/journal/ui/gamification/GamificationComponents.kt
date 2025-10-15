package com.isaakhanimann.journal.ui.gamification

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.gamification.Achievement
import com.isaakhanimann.journal.data.gamification.AIPersonalizedInsight
import com.isaakhanimann.journal.data.gamification.KnowledgeQuest
import com.isaakhanimann.journal.data.gamification.UserLevel
import com.isaakhanimann.journal.data.gamification.WeeklyChallenge

@Composable
fun UserLevelProgress(userLevel: UserLevel) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Level: ${userLevel.level}", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = userLevel.xp.toFloat() / userLevel.nextLevelXp.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${userLevel.xp} / ${userLevel.nextLevelXp} XP",
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            // Placeholder for an icon
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(achievement.name, style = MaterialTheme.typography.titleMedium)
                Text(achievement.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun QuestCard(quest: KnowledgeQuest) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(quest.title, style = MaterialTheme.typography.titleMedium)
            Text(quest.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { /* TODO */ }) {
                Text("Start Quest")
            }
        }
    }
}

@Composable
fun ChallengeCard(challenge: WeeklyChallenge) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(challenge.title, style = MaterialTheme.typography.titleMedium)
            Text(challenge.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = challenge.progress.toFloat() / challenge.target.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "${challenge.progress} / ${challenge.target}",
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun InsightCard(insight: AIPersonalizedInsight) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = insight.insightType.name.replace('_', ' '),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = insight.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}