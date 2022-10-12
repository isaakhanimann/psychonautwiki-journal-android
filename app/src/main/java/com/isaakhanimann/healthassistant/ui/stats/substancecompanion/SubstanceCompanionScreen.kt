package com.isaakhanimann.healthassistant.ui.stats.substancecompanion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.substances.classes.Tolerance
import com.isaakhanimann.healthassistant.ui.addingestion.time.ColorPicker
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.roa.ToleranceSection
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern

@Composable
fun SubstanceCompanionScreen(
    viewModel: SubstanceCompanionViewModel = hiltViewModel()
) {
    viewModel.thisCompanionFlow.collectAsState().value?.also { companion ->
        SubstanceCompanionScreen(
            substanceCompanion = companion,
            ingestionBursts = viewModel.ingestionBurstsFlow.collectAsState().value,
            onChangeColor = { viewModel.updateColor(it) },
            alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
            otherColors = viewModel.otherColorsFlow.collectAsState().value,
            tolerance = viewModel.tolerance,
            crossTolerances = viewModel.crossTolerances
        )
    }
}

@Preview
@Composable
fun SubstanceCompanionPreview(@PreviewParameter(SubstanceCompanionScreenPreviewProvider::class) pair: Pair<SubstanceCompanion, List<IngestionsBurst>>) {
    val alreadyUsedColors = listOf(SubstanceColor.BLUE, SubstanceColor.PINK)
    val otherColors = SubstanceColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    HealthAssistantTheme {
        SubstanceCompanionScreen(
            substanceCompanion = pair.first,
            ingestionBursts = pair.second,
            onChangeColor = {},
            alreadyUsedColors = alreadyUsedColors,
            otherColors = otherColors,
            tolerance = Tolerance(
                full = "with prolonged use",
                half = "two weeks",
                zero = "1 month"
            ),
            crossTolerances = listOf(
                "dopamine",
                "stimulant"
            ),
        )
    }
}

@Composable
fun SubstanceCompanionScreen(
    substanceCompanion: SubstanceCompanion,
    ingestionBursts: List<IngestionsBurst>,
    onChangeColor: (SubstanceColor) -> Unit,
    alreadyUsedColors: List<SubstanceColor>,
    otherColors: List<SubstanceColor>,
    tolerance: Tolerance?,
    crossTolerances: List<String>
) {
    Scaffold(
        topBar = {
            JournalTopAppBar(title = substanceCompanion.substanceName)
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                if (tolerance != null || crossTolerances.isNotEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SectionTitle(title = "Tolerance")
                        ToleranceSection(
                            tolerance = tolerance,
                            crossTolerances = crossTolerances,
                            modifier = Modifier.padding(
                                vertical = 5.dp,
                                horizontal = horizontalPadding
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                SectionTitle(title = "${substanceCompanion.substanceName} Color")
                Spacer(modifier = Modifier.height(6.dp))
                ColorPicker(
                    selectedColor = substanceCompanion.color,
                    onChangeOfColor = onChangeColor,
                    alreadyUsedColors = alreadyUsedColors,
                    otherColors = otherColors
                )
                Spacer(modifier = Modifier.height(6.dp))
                SectionTitle(title = "History")
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Now")
                Spacer(modifier = Modifier.height(4.dp))
            }
            ingestionBursts.forEach { burst ->
                item {
                    TimeArrowUp(timeText = burst.timeUntil)
                    Divider()
                }
                items(burst.ingestions.size) { i ->
                    val ingestion = burst.ingestions[i]
                    IngestionRow(ingestion = ingestion)
                    Divider()
                }
            }
        }
    }
}

@Composable
fun IngestionRow(ingestion: Ingestion) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        ingestion.dose?.also {
            Text(
                text = "${if (ingestion.isDoseAnEstimate) "~" else ""}${it.toReadableString()} ${ingestion.units} ${ingestion.administrationRoute.displayText}",
            )
        } ?: run {
            Text(
                text = "Unknown Dose ${ingestion.administrationRoute.displayText}",
            )
        }
        val dateString = ingestion.time.getStringOfPattern("dd MMM yyyy, HH:mm")
        Text(text = dateString)
    }
}