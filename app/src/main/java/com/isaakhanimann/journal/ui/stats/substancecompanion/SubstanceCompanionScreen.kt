package com.isaakhanimann.journal.ui.stats.substancecompanion

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.substances.classes.Tolerance
import com.isaakhanimann.journal.ui.addingestion.time.ColorPicker
import com.isaakhanimann.journal.ui.journal.experience.CardWithTitle
import com.isaakhanimann.journal.ui.search.substance.roa.ToleranceSection
import com.isaakhanimann.journal.ui.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getStringOfPattern

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
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
    val otherColors = AdaptiveColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    JournalTheme {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstanceCompanionScreen(
    substanceCompanion: SubstanceCompanion,
    ingestionBursts: List<IngestionsBurst>,
    onChangeColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>,
    tolerance: Tolerance?,
    crossTolerances: List<String>
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(substanceCompanion.substanceName) })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = horizontalPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(modifier = Modifier.padding(vertical = 5.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 5.dp
                        )
                    ) {
                        Text(text = "Color", style = MaterialTheme.typography.titleMedium)
                        ColorPicker(
                            selectedColor = substanceCompanion.color,
                            onChangeOfColor = onChangeColor,
                            alreadyUsedColors = alreadyUsedColors,
                            otherColors = otherColors
                        )
                    }
                }
                if (tolerance != null || crossTolerances.isNotEmpty()) {
                    CardWithTitle(title = "Tolerance") {
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
                Text(text = "Now")
            }
            items(ingestionBursts) { burst ->
                TimeArrowUp(timeText = burst.timeUntil)
                Card(modifier = Modifier.padding(vertical = 5.dp)) {
                    Column(modifier = Modifier.padding(horizontal = horizontalPadding)) {
                        burst.ingestions.forEachIndexed { index, ingestion ->
                            IngestionRow(ingestion = ingestion)
                            if (index < burst.ingestions.size - 1) {
                                Divider()
                            }
                        }
                    }
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
            .padding(vertical = 4.dp),
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