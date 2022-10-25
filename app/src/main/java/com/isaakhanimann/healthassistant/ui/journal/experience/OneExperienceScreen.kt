package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.journal.experience.timeline.AllTimelines
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern
import java.time.Instant

@Composable
fun ExperienceScreen(
    viewModel: OneExperienceViewModel = hiltViewModel(),
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
) {
    viewModel.oneExperienceScreenModelFlow.collectAsState().value?.also { oneExperienceScreenModel ->
        ExperienceScreen(
            oneExperienceScreenModel = oneExperienceScreenModel,
            addIngestion = navigateToAddIngestionSearch,
            deleteExperience = viewModel::deleteExperience,
            navigateToEditExperienceScreen = navigateToEditExperienceScreen,
            navigateToExplainTimeline = navigateToExplainTimeline,
            navigateToIngestionScreen = navigateToIngestionScreen,
            navigateBack = navigateBack,
            saveIsFavorite = viewModel::saveIsFavorite
        )
    }
}

@Preview
@Composable
fun ExperienceScreenPreview(
    @PreviewParameter(
        OneExperienceScreenPreviewProvider::class,
        limit = 1
    ) oneExperienceScreenModel: OneExperienceScreenModel
) {
    HealthAssistantTheme {
        ExperienceScreen(
            oneExperienceScreenModel = oneExperienceScreenModel,
            addIngestion = {},
            deleteExperience = {},
            navigateToEditExperienceScreen = {},
            navigateToExplainTimeline = {},
            navigateToIngestionScreen = {},
            navigateBack = {},
            saveIsFavorite = {}
        )
    }
}

@Composable
fun ExperienceScreen(
    oneExperienceScreenModel: OneExperienceScreenModel,
    addIngestion: () -> Unit,
    deleteExperience: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateBack: () -> Unit,
    saveIsFavorite: (Boolean) -> Unit
) {
    Scaffold(
        topBar = {
            JournalTopAppBar(
                title = oneExperienceScreenModel.title,
                actions = {
                    IconButton(onClick = navigateToEditExperienceScreen) {
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Experience",
                        )
                    }
                    val isFavorite = oneExperienceScreenModel.isFavorite
                    IconToggleButton(checked = isFavorite, onCheckedChange = saveIsFavorite) {
                        if (isFavorite) {
                            Icon(Icons.Filled.Star, contentDescription = "Is Favorite")
                        } else {
                            Icon(Icons.Outlined.StarOutline, contentDescription = "Is not Favorite")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (oneExperienceScreenModel.isShowingAddIngestionButton) {
                ExtendedFloatingActionButton(
                    onClick = addIngestion,
                    icon = {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "Add"
                        )
                    },
                    text = { Text("Ingestion") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            val elements = oneExperienceScreenModel.ingestionElements
            val effectTimelines = remember(elements) {
                elements.map { oneElement ->
                    return@map DataForOneEffectLine(
                        roaDuration = oneElement.roaDuration,
                        height = getHeightBetween0And1(
                            ingestion = oneElement.ingestionWithCompanion.ingestion,
                            allIngestions = elements.map { it.ingestionWithCompanion.ingestion }
                        ),
                        color = oneElement.ingestionWithCompanion.substanceCompanion?.color
                            ?: AdaptiveColor.RED,
                        startTime = oneElement.ingestionWithCompanion.ingestion.time
                    )
                }
            }
            if (effectTimelines.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                    AllTimelines(
                        dataForEffectLines = effectTimelines,
                        isShowingCurrentTime = true,
                        navigateToExplainTimeline = navigateToExplainTimeline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    SectionTitle(
                        title = oneExperienceScreenModel.firstIngestionTime.getStringOfPattern(
                            "EEE, dd MMM yyyy"
                        )
                    )
                    oneExperienceScreenModel.ingestionElements.forEachIndexed { index, ingestionElement ->
                        IngestionRow(
                            ingestionElement = ingestionElement,
                            modifier = Modifier
                                .clickable {
                                    navigateToIngestionScreen(ingestionElement.ingestionWithCompanion.ingestion.id)
                                }
                                .fillMaxWidth()
                                .padding(vertical = 5.dp, horizontal = horizontalPadding)
                        )
                        if (index < oneExperienceScreenModel.ingestionElements.size - 1) {
                            Divider()
                        }
                    }
                }
            }
            val cumulativeDoses = oneExperienceScreenModel.cumulativeDoses
            if (cumulativeDoses.isNotEmpty()) {
                SectionTitle(title = "Cumulative Dose")
                cumulativeDoses.forEachIndexed { index, cumulativeDose ->
                    CumulativeDoseRow(
                        cumulativeDose = cumulativeDose, modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp, horizontal = horizontalPadding)
                    )
                    if (index < cumulativeDoses.size - 1) {
                        Divider()
                    }
                }
            }
            SectionTitle(title = "Notes")
            if (oneExperienceScreenModel.notes.isEmpty()) {
                TextButton(
                    onClick = navigateToEditExperienceScreen,
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Add Notes")
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = horizontalPadding)
                ) {
                    Text(
                        text = oneExperienceScreenModel.notes,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    IconButton(onClick = navigateToEditExperienceScreen) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Notes")
                    }
                }
            }
            var isShowingDeleteDialog by remember { mutableStateOf(false) }
            TextButton(
                onClick = { isShowingDeleteDialog = true },
                modifier = Modifier.padding(horizontal = 5.dp)
            ) {
                Text(
                    "Delete Experience",
                    style = MaterialTheme.typography.caption
                )
            }
            if (isShowingDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { isShowingDeleteDialog = false },
                    title = {
                        Text(text = "Delete Experience?")
                    },
                    text = {
                        Text("This will also delete all its ingestions.")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isShowingDeleteDialog = false
                                deleteExperience()
                                navigateBack()
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isShowingDeleteDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}

data class DataForOneEffectLine(
    val roaDuration: RoaDuration?,
    val height: Float,
    val color: AdaptiveColor,
    val startTime: Instant
)

fun getHeightBetween0And1(
    ingestion: Ingestion,
    allIngestions: List<Ingestion>
): Float {
    val max = allIngestions
        .filter { it.substanceName == ingestion.substanceName }
        .mapNotNull { it.dose }
        .maxOrNull()
    return ingestion.dose.let { doseSnap ->
        if (max == null || doseSnap == null) {
            1f
        } else {
            doseSnap.div(max).toFloat()
        }
    }
}

@Composable
fun CumulativeDoseRow(cumulativeDose: CumulativeDose, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = cumulativeDose.substanceName, style = MaterialTheme.typography.h6)
        Column(horizontalAlignment = Alignment.End) {
            Text(text = (if (cumulativeDose.isEstimate) "~" else "") + cumulativeDose.cumulativeDose.toReadableString() + " " + cumulativeDose.units)
            val doseClass = cumulativeDose.doseClass
            if (doseClass != null) {
                DotRow(doseClass = doseClass)
            }
        }
    }
}