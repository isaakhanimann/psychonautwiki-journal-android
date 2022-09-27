package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern

@Preview(showBackground = true)
@Composable
fun ExperienceRow(
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
            .padding(horizontal = 8.dp, vertical = 8.dp),
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
                    style = MaterialTheme.typography.h5,
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        val substanceNames = remember(ingestions) {
                            ingestions.map { it.ingestion.substanceName }.distinct()
                                .joinToString(separator = ", ")
                        }
                        if (substanceNames.isNotEmpty()) {
                            Text(text = substanceNames, style = MaterialTheme.typography.subtitle1)
                        } else {
                            Text(
                                text = "No substance yet",
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        if (experience.text.isNotBlank()) {
                            Icon(
                                Icons.Outlined.StickyNote2,
                                contentDescription = "Experience has notes"
                            )
                        }
                    }
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxHeight()
        ) {
            Row {
                val experience = experienceWithIngestionsAndCompanions.experience
                experience.sentiment?.let {
                    Icon(imageVector = it.icon, contentDescription = it.description)
                }
                if (experience.isFavorite) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = "Is Favorite")
                }
            }
            val dateText = remember(experienceWithIngestionsAndCompanions.sortInstant) {
                experienceWithIngestionsAndCompanions.sortInstant.getStringOfPattern("dd MMM yy")
            }
            Text(text = dateText)
        }
    }
}

@Composable
fun ExperienceCircle(ingestions: List<IngestionWithCompanion>) {
    val isDarkTheme = isSystemInDarkTheme()
    val circleSize = 40.dp
    if (ingestions.size >= 2) {
        val brush = remember(ingestions) {
            val colors =
                ingestions.map { it.substanceCompanion!!.color.getComposeColor(isDarkTheme) }
            val twiceColors = colors.flatMap { listOf(it, it) } + colors.first()
            Brush.sweepGradient(twiceColors)
        }
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(brush),
        ) {}
    } else if (ingestions.size == 1) {
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(
                    ingestions.first().substanceCompanion!!.color.getComposeColor(
                        isDarkTheme
                    )
                ),
        ) {}
    } else {
        Box(
            modifier = Modifier
                .size(circleSize)
                .clip(CircleShape)
                .background(Color.LightGray.copy(0.1f)),
        ) {}
    }
}