package com.example.healthassistant.ui.ingestions.ingestion

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    navigateToEditNote: () -> Unit,
    navigateToEditMembership: () -> Unit,
    navigateBack: () -> Unit
) {
    viewModel.ingestionWithDurationAndExperience.collectAsState().value?.also { ingestionWithDurationAndExperience ->
        OneIngestionScreen(
            ingestionWithDurationAndExperience,
            navigateToEditNote,
            navigateToEditMembership,
            navigateBack,
            viewModel::deleteIngestion
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
            navigateToEditNote = {},
            navigateToEditMembership = {},
            navigateBack = {},
            deleteIngestion = {}
        )
    }
}

@Composable
fun OneIngestionScreen(
    ingestionWithDurationAndExperience: IngestionWithDurationAndExperience,
    navigateToEditNote: () -> Unit,
    navigateToEditMembership: () -> Unit,
    navigateBack: () -> Unit,
    deleteIngestion: () -> Unit
) {
    val ingestion = ingestionWithDurationAndExperience.ingestion
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                    val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
                    Text(text = timeString)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AllTimelines(
                ingestionDurationPairs = listOf(Pair(first = ingestion, second = ingestionWithDurationAndExperience.roaDuration)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            val showOralOnsetDisclaimer = ingestion.administrationRoute == AdministrationRoute.ORAL
            if (showOralOnsetDisclaimer) {
                Text(
                    text = "* a full stomach can delay the onset by hours",
                    style = MaterialTheme.typography.caption
                )
            }
            Divider(modifier = Modifier.padding(top = 10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(vertical = 10.dp)
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
            Divider()
            ingestion.notes.let {
                val constNote = it
                if (constNote.isNullOrBlank()) {
                    TextButton(onClick = navigateToEditNote) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Add Note")
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = constNote, modifier = Modifier.weight(1f))
                        IconButton(onClick = navigateToEditNote) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                    }
                }
            }
            Divider()
            val experience = ingestionWithDurationAndExperience.experience
            if (experience == null) {
                TextButton(onClick = navigateToEditMembership) {
                    Text(text = "Assign to Experience")
                }
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Part of ${experience.title}", modifier = Modifier.weight(1f))
                    IconButton(onClick = navigateToEditMembership) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                }
            }
            Divider()
            TextButton(
                onClick = {
                    deleteIngestion()
                    navigateBack()
                },
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete Ingestion",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = Color.Red
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Delete", color = Color.Red)
            }
            Divider()
        }
    }
}