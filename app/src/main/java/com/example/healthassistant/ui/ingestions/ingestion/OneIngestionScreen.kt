package com.example.healthassistant.ui.ingestions.ingestion

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.experiences.experience.timeline.AllTimelines
import com.example.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import com.example.healthassistant.ui.theme.HealthAssistantTheme
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun OneIngestionScreen(
    viewModel: OneIngestionViewModel = hiltViewModel(),
    navigateToEditNote: () -> Unit,
    navigateToEditMembership: () -> Unit,
    navigateToSubstance: (substanceName: String) -> Unit,
    navigateToDoseExplanationScreen: () -> Unit,
    navigateBack: () -> Unit
) {
    viewModel.ingestionWithCompanionDurationAndExperience.collectAsState().value?.also { ingestionWithDurationAndExperience ->
        OneIngestionScreen(
            ingestionWithCompanionDurationAndExperience = ingestionWithDurationAndExperience,
            navigateToEditNote = navigateToEditNote,
            navigateToEditMembership = navigateToEditMembership,
            navigateToSubstance = navigateToSubstance,
            navigateToDoseExplanationScreen = navigateToDoseExplanationScreen,
            navigateBack = navigateBack,
            deleteIngestion = viewModel::deleteIngestion,
            isShowingDialog = viewModel.isShowingDeleteDialog,
            showDialog = { viewModel.isShowingDeleteDialog = true },
            dismissDialog = { viewModel.isShowingDeleteDialog = false },
            isShowingEditSentiment = viewModel.isShowingSentimentMenu,
            showSentimentMenu = viewModel::showEditSentimentDialog,
            dismissSentimentMenu = viewModel::dismissEditSentimentDialog,
            saveSentiment = viewModel::saveSentiment,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OneIngestionScreenPreview(
    @PreviewParameter(
        IngestionWithDurationAndExperienceProvider::class,
        limit = 1
    ) ingestionWithCompanionDurationAndExperience: OneIngestionViewModel.IngestionWithCompanionDurationAndExperience
) {
    HealthAssistantTheme {
        OneIngestionScreen(
            ingestionWithCompanionDurationAndExperience = ingestionWithCompanionDurationAndExperience,
            navigateToEditNote = {},
            navigateToEditMembership = {},
            navigateToSubstance = {},
            navigateToDoseExplanationScreen = {},
            navigateBack = {},
            deleteIngestion = {},
            isShowingDialog = false,
            showDialog = {},
            dismissDialog = {},
            isShowingEditSentiment = false,
            showSentimentMenu = {},
            dismissSentimentMenu = {},
            saveSentiment = {},
        )
    }
}

@Composable
fun OneIngestionScreen(
    ingestionWithCompanionDurationAndExperience: OneIngestionViewModel.IngestionWithCompanionDurationAndExperience,
    navigateToEditNote: () -> Unit,
    navigateToEditMembership: () -> Unit,
    navigateToSubstance: (substanceName: String) -> Unit,
    navigateToDoseExplanationScreen: () -> Unit,
    navigateBack: () -> Unit,
    deleteIngestion: () -> Unit,
    isShowingDialog: Boolean,
    showDialog: () -> Unit,
    dismissDialog: () -> Unit,
    isShowingEditSentiment: Boolean,
    showSentimentMenu: () -> Unit,
    dismissSentimentMenu: () -> Unit,
    saveSentiment: (sentiment: Sentiment?) -> Unit
) {
    val ingestionWithCompanion = ingestionWithCompanionDurationAndExperience.ingestionWithCompanion
    val ingestion = ingestionWithCompanion.ingestion
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
                .verticalScroll(rememberScrollState())
        ) {
            val horizontalPadding = 10.dp
            Spacer(modifier = Modifier.height(10.dp))
            Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                AllTimelines(
                    ingestionDurationPairs = listOf(
                        Pair(
                            first = ingestionWithCompanion,
                            second = ingestionWithCompanionDurationAndExperience.roaDuration
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                val showOralOnsetDisclaimer =
                    ingestion.administrationRoute == AdministrationRoute.ORAL
                if (showOralOnsetDisclaimer) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "* a full stomach can delay the onset for hours",
                        style = MaterialTheme.typography.caption
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
            Divider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .clickable {
                        navigateToSubstance(ingestion.substanceName)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = horizontalPadding)
            ) {
                val isDarkTheme = isSystemInDarkTheme()
                Surface(
                    shape = CircleShape,
                    color = ingestionWithCompanionDurationAndExperience.ingestionWithCompanion.substanceCompanion!!.color.getComposeColor(
                        isDarkTheme
                    ),
                    modifier = Modifier.size(30.dp)
                ) {}
                Text(
                    text = ingestion.substanceName,
                    style = MaterialTheme.typography.h6
                )
            }
            Divider()
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.padding(vertical = 10.dp, horizontal = horizontalPadding)
            ) {
                Text(
                    text = "${ingestion.administrationRoute.displayText} Dose",
                    style = MaterialTheme.typography.subtitle2
                )
                val roaDose = ingestionWithCompanionDurationAndExperience.roaDose
                if (roaDose != null) {
                    RoaDoseView(
                        roaDose = roaDose,
                        navigateToDosageExplanationScreen = navigateToDoseExplanationScreen
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Consumed Dose")
                    ingestion.dose?.also {
                        val estimateText = if (ingestion.isDoseAnEstimate) "~" else ""
                        val doseClass = roaDose?.getDoseClass(ingestion.dose, ingestion.units)
                        val doseTextColor = doseClass?.getComposeColor(isSystemInDarkTheme())
                            ?: MaterialTheme.colors.onBackground
                        Text(
                            text = "$estimateText${it.toReadableString()} ${ingestion.units}",
                            style = MaterialTheme.typography.h5,
                            color = doseTextColor,
                        )
                    } ?: run {
                        Text(
                            text = "Unknown Dose",
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                }
            }
            Divider()
            SentimentSection(
                sentiment = ingestion.sentiment,
                isShowingEditSentiment = isShowingEditSentiment,
                show = showSentimentMenu,
                dismiss = dismissSentimentMenu,
                saveSentiment = saveSentiment,
                modifier = Modifier.padding(start = horizontalPadding)
            )
            Divider()
            ingestion.notes.let {
                val constNote = it
                if (constNote.isNullOrBlank()) {
                    TextButton(
                        onClick = navigateToEditNote,
                        modifier = Modifier.padding(horizontal = 5.dp)
                    ) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Add Note")
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = horizontalPadding)
                    ) {
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
                Divider()
            }
            val experience = ingestionWithCompanionDurationAndExperience.experience
            if (experience != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text(text = "Part of ${experience.title}", modifier = Modifier.weight(1f))
                    IconButton(onClick = navigateToEditMembership) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                }
                Divider()
            }
            Column(modifier = Modifier.padding(horizontal = 5.dp)) {
                if (experience == null) {
                    TextButton(onClick = navigateToEditMembership) {
                        Text(
                            text = "Assign to Experience",
                            style = MaterialTheme.typography.caption
                        )
                    }
                }
                TextButton(
                    onClick = showDialog,
                ) {
                    Text("Delete Ingestion", style = MaterialTheme.typography.caption)
                }
            }
            if (isShowingDialog) {
                AlertDialog(
                    onDismissRequest = dismissDialog,
                    title = {
                        Text(text = "Delete Ingestion?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                dismissDialog()
                                deleteIngestion()
                                navigateBack()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = dismissDialog
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}