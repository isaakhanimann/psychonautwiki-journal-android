package com.example.healthassistant.ui.experiences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.example.healthassistant.data.room.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun ExperienceRow(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) experienceWithIngestions: ExperienceWithIngestions,
    navigateToExperienceScreen: () -> Unit = {},
    navigateToEditExperienceScreen: () -> Unit = {},
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
            val isDarkTheme = isSystemInDarkTheme()
            val circleSize = 45.dp
            val ingestions = experienceWithIngestions.ingestions
            if (ingestions.size >= 2) {
                val brush = remember(ingestions) {
                    val colors =
                        ingestions.map { it.color.getComposeColor(isDarkTheme) }
                    val twiceColors = colors.flatMap { listOf(it, it) } + colors.first()
                    Brush.sweepGradient(twiceColors)
                }
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(brush)
                )
            } else if (ingestions.size == 1) {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(
                            ingestions.first().color.getComposeColor(
                                isDarkTheme
                            )
                        )
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(Color.LightGray.copy(0.1f))
                )
            }
            Column {
                Text(
                    text = experienceWithIngestions.experience.title,
                    style = MaterialTheme.typography.h5,
                )
                val substanceNames = remember(ingestions) {
                    ingestions.map { it.substanceName }.distinct()
                        .joinToString(separator = ", ")
                }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    if (substanceNames.isNotEmpty()) {
                        Text(text = substanceNames, style = MaterialTheme.typography.subtitle1)
                    } else {
                        Text(text = "No substance yet", style = MaterialTheme.typography.subtitle1)
                    }
                }
            }
        }
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val dateText = remember(experienceWithIngestions.sortDate) {
                val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
                formatter.format(experienceWithIngestions.sortDate) ?: ""
            }
            Text(text = dateText)
        }
    }
}