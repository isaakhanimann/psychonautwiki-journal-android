package com.example.healthassistant.ui.ingestions.ingestion

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.experiences.experience.timeline.AllTimelines
import com.example.healthassistant.ui.previewproviders.IngestionWithDurationAndExperienceProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import com.example.healthassistant.ui.theme.HealthAssistantTheme
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun OneIngestionScreen(
    viewModel: OneIngestionViewModel = hiltViewModel(),
    navigateToEditIngestionScreen: () -> Unit,
) {
    viewModel.ingestionWithDurationAndExperience.collectAsState().value?.also { ingestionWithDurationAndExperience ->
        OneIngestionScreen(
            ingestionWithDurationAndExperience = ingestionWithDurationAndExperience,
            navigateToEditIngestionScreen = navigateToEditIngestionScreen
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OneIngestionScreenPreview(
    @PreviewParameter(
        IngestionWithDurationAndExperienceProvider::class,
        limit = 1
    ) ingestionWithDurationAndExperience: IngestionWithDurationAndExperience
) {
    HealthAssistantTheme {
        OneIngestionScreen(
            ingestionWithDurationAndExperience = ingestionWithDurationAndExperience,
            navigateToEditIngestionScreen = {})
    }
}

@Composable
fun OneIngestionScreen(
    ingestionWithDurationAndExperience: IngestionWithDurationAndExperience,
    navigateToEditIngestionScreen: () -> Unit,
) {
    val ingestion = ingestionWithDurationAndExperience.ingestion
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                    val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
                    Text(text = timeString)
                },
                actions = {
                    IconButton(onClick = navigateToEditIngestionScreen) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Ingestion")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val spacingBetweenSections = 20.dp
            AllTimelines(
                ingestionDurationPairs = listOf(Pair(first = ingestion, second = ingestionWithDurationAndExperience.roaDuration)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            val showOralOnsetDisclaimer = ingestion.administrationRoute == AdministrationRoute.ORAL
            if (showOralOnsetDisclaimer) {
                Text(
                    text = "* a full stomach can delay the onset significantly",
                    style = MaterialTheme.typography.caption
                )
            }
            Divider(modifier = Modifier.padding(vertical = spacingBetweenSections))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val isDarkTheme = isSystemInDarkTheme()
                    Surface(
                        shape = CircleShape,
                        color = ingestion.color.getComposeColor(isDarkTheme),
                        modifier = Modifier.size(25.dp)
                    ) {}
                    Column {
                        Text(
                            text = ingestion.substanceName,
                            style = MaterialTheme.typography.h6
                        )
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                            ingestion.dose?.also {
                                Text(
                                    text = "${if (ingestion.isDoseAnEstimate) "~" else ""}${it.toReadableString()} ${ingestion.units} ${ingestion.administrationRoute.displayText}",
                                    style = MaterialTheme.typography.subtitle1
                                )
                            } ?: run {
                                Text(
                                    text = "Unknown Dose ${ingestion.administrationRoute.displayText}",
                                    style = MaterialTheme.typography.subtitle1
                                )
                            }
                        }
                    }
                }
            }
            Divider(modifier = Modifier.padding(vertical = spacingBetweenSections))
            if (ingestion.notes.isNullOrBlank()) {
                TextButton(onClick = navigateToEditIngestionScreen) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add Note")
                }
            } else {
                Text(text = ingestion.notes)
            }
            Divider(modifier = Modifier.padding(vertical = spacingBetweenSections))
            val experience = ingestionWithDurationAndExperience.experience
            if (experience == null) {
                Column {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Create New Experience With This Ingestion")
                    }
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Assign to Existing Experience")
                    }
                }
            } else {
                Column {
                    Text(text = "Part of ${experience.title}")
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "Edit")
                    }
                }
            }
        }
    }
}