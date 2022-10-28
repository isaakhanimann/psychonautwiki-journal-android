package com.isaakhanimann.healthassistant.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.healthassistant.ui.journal.ExperienceCircle
import com.isaakhanimann.healthassistant.ui.journal.ExperienceWithIngestionsPreviewProvider
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding


@Preview(showBackground = true)
@Composable
fun CurrentExperienceRow(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) experienceWithIngestionsAndCompanions: ExperienceWithIngestionsAndCompanions,
    navigateToExperienceScreen: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .clickable {
                navigateToExperienceScreen()
            }
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val ingestions = experienceWithIngestionsAndCompanions.ingestionsWithCompanions
        val experience = experienceWithIngestionsAndCompanions.experience
        ExperienceCircle(ingestions = ingestions, circleSize = 25.dp)
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = experience.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(2f)
        )
        Spacer(modifier = Modifier.width(5.dp))
        val substanceNames = remember(ingestions) {
            ingestions.map { it.ingestion.substanceName }.distinct()
                .joinToString(separator = ", ")
        }
        val substanceText =
            substanceNames.ifEmpty { "No substance yet" }
        Text(
            text = substanceText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}