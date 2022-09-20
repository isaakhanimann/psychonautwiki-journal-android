package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString


@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    navigateToSubstanceCompanion: (substanceName: String) -> Unit
) {
    StatsScreen(
        substancesLastUsed = viewModel.substanceStats.collectAsState().value,
        navigateToSubstanceCompanion = navigateToSubstanceCompanion,
        selectedOption = viewModel.optionFlow.collectAsState().value,
        onTapOption = viewModel::onTapOption,
        startDateText = viewModel.startDateTextFlow.collectAsState().value
    )
}

@Preview
@Composable
fun StatsPreview(
    @PreviewParameter(
        StatsPreviewProvider::class,
    ) substancesLastUsed: List<SubstanceStat>
) {
    StatsScreen(
        substancesLastUsed = substancesLastUsed,
        navigateToSubstanceCompanion = {},
        selectedOption = TimePickerOption.DAYS_30,
        onTapOption = {},
        startDateText = "22. June 2022"
    )
}

@Composable
fun StatsScreen(
    substancesLastUsed: List<SubstanceStat>,
    navigateToSubstanceCompanion: (substanceName: String) -> Unit,
    selectedOption: TimePickerOption,
    onTapOption: (option: TimePickerOption) -> Unit,
    startDateText: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ingestion Statistics") },
                elevation = 0.dp
            )
        }
    ) {
        if (substancesLastUsed.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "Nothing to Show Yet")
                }
            }
        } else {
            Column {
                TabRow(selectedTabIndex = selectedOption.tabIndex) {
                    TimePickerOption.values().forEachIndexed { index, option ->
                        Tab(
                            text = { Text(option.displayText) },
                            selected = selectedOption.tabIndex == index,
                            onClick = { onTapOption(option) }
                        )
                    }
                }
                Text(
                    text = "Since $startDateText",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(10.dp)
                )
                Divider()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(substancesLastUsed.size) { i ->
                        val subStat = substancesLastUsed[i]
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navigateToSubstanceCompanion(subStat.substanceName)
                                }
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            val isDarkTheme = isSystemInDarkTheme()
                            Surface(
                                shape = CircleShape,
                                color = subStat.color.getComposeColor(isDarkTheme),
                                modifier = Modifier.size(25.dp)
                            ) {}
                            Column {
                                Text(text = subStat.substanceName, style = MaterialTheme.typography.h6)
                                val cumulativeDose = subStat.cumulativeDose
                                if (cumulativeDose != null) {
                                    Text(text = "Cumulative dose: ${if (cumulativeDose.isEstimate) "~" else ""} ${cumulativeDose.dose.toReadableString()} ${cumulativeDose.units}")
                                } else {
                                    Text(text = "Cumulative dose unknown")
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Column {
                                Text(
                                    text = subStat.ingestionCount.toString() + if (subStat.ingestionCount == 1) " Ingestion" else " Ingestions",
                                    style = MaterialTheme.typography.subtitle2
                                )
                                subStat.routeCounts.forEach {
                                    Text(text = "${it.count}x ${it.administrationRoute.displayText}")
                                }
                            }

                        }
                        if (i < substancesLastUsed.size) {
                            Divider()
                        }
                    }
                }
            }


        }
    }
}