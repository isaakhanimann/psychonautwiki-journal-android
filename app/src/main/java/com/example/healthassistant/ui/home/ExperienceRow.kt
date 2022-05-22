package com.example.healthassistant.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.previewproviders.ExperienceWithIngestionsPreviewProvider
import java.text.SimpleDateFormat
import java.util.*

@Preview(showBackground = true)
@Composable
fun ExperienceRow(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) experienceWithIngs: ExperienceWithIngestions,
    navigateToExperienceScreen: () -> Unit = {},
    deleteExperienceWithIngestions: () -> Unit = {}
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
            if (experienceWithIngs.ingestions.size >= 2) {
                val brush = remember(experienceWithIngs.ingestions) {
                    val colors =
                        experienceWithIngs.ingestions.map { it.color.getComposeColor(isDarkTheme) }
                    Brush.radialGradient(colors)
                }
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(brush)
                )
            } else if (experienceWithIngs.ingestions.size == 1) {
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .clip(CircleShape)
                        .background(
                            experienceWithIngs.ingestions.first().color.getComposeColor(
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
                    text = experienceWithIngs.experience.title,
                    style = MaterialTheme.typography.h5,
                )
                val substanceNames = remember(experienceWithIngs.ingestions) {
                    experienceWithIngs.ingestions.map { it.substanceName }.distinct()
                        .joinToString(separator = ", ")
                }
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    if (substanceNames.isNotEmpty()) {
                        Text(text = substanceNames)
                    } else {
                        Text(text = "No substance yet")
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                val dateText = remember(experienceWithIngs.experience.date) {
                    val formatter = SimpleDateFormat("dd MMMM", Locale.getDefault())
                    formatter.format(experienceWithIngs.experience.date) ?: ""
                }
                Text(dateText)
            }
            var isExpanded by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopStart).fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { isExpanded = true },
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                }
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            deleteExperienceWithIngestions()
                            isExpanded = false
                        }
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    }
}