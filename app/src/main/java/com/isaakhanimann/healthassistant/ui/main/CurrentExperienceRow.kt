package com.isaakhanimann.healthassistant.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            .height(IntrinsicSize.Max)
            .padding(horizontal = horizontalPadding, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            val ingestions = experienceWithIngestionsAndCompanions.ingestionsWithCompanions
            val experience = experienceWithIngestionsAndCompanions.experience
            ExperienceCircle(ingestions = ingestions)
            Column {
                Text(
                    text = experience.title,
                    style = MaterialTheme.typography.h6,
                )
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    val substanceNames = remember(ingestions) {
                        ingestions.map { it.ingestion.substanceName }.distinct()
                            .joinToString(separator = ", ")
                    }
                    if (substanceNames.isNotEmpty()) {
                        Text(text = substanceNames)
                    } else {
                        Text(
                            text = "No substance yet",
                        )
                    }
                }
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Right")
    }
}