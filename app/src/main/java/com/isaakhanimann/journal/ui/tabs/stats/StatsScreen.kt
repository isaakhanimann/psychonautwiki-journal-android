/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.stats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.ui.YOU
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.toReadableString
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding


@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    navigateToSubstanceCompanion: (substanceName: String, consumerName: String?) -> Unit,
) {
    StatsScreen(
        navigateToSubstanceCompanion = navigateToSubstanceCompanion,
        onTapOption = viewModel::onTapOption,
        statsModel = viewModel.statsModelFlow.collectAsState().value,
        onChangeConsumerName = viewModel::onChangeConsumer,
        consumerNamesSorted = viewModel.sortedConsumerNamesFlow.collectAsState().value
    )
}

@Preview
@Composable
fun StatsPreview(
    @PreviewParameter(
        StatsPreviewProvider::class,
    ) statsModel: StatsModel
) {
    JournalTheme {
        StatsScreen(
            navigateToSubstanceCompanion = { _, _ -> },
            onTapOption = {},
            statsModel = statsModel,
            onChangeConsumerName = {},
            consumerNamesSorted = listOf("You", "Someone else")
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(
    navigateToSubstanceCompanion: (substanceName: String, consumerName: String?) -> Unit,
    onTapOption: (option: TimePickerOption) -> Unit,
    statsModel: StatsModel,
    onChangeConsumerName: (String?) -> Unit,
    consumerNamesSorted: List<String>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (statsModel.consumerName == null) "Statistics" else "Statistics for ${statsModel.consumerName}") },
                actions = {
                    if (consumerNamesSorted.isNotEmpty()) {
                        var isConsumerSelectionExpanded by remember { mutableStateOf(false) }
                        IconButton(onClick = { isConsumerSelectionExpanded = true }) {
                            Icon(Icons.Outlined.Person, contentDescription = "Consumer")
                        }
                        DropdownMenu(
                            expanded = isConsumerSelectionExpanded,
                            onDismissRequest = { isConsumerSelectionExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(YOU) },
                                onClick = {
                                    onChangeConsumerName(null)
                                    isConsumerSelectionExpanded = false
                                },
                                leadingIcon = {
                                    if (statsModel.consumerName == null) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Check",
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    }
                                }
                            )
                            consumerNamesSorted.forEach { consumerName ->
                                DropdownMenuItem(
                                    text = { Text(consumerName) },
                                    onClick = {
                                        onChangeConsumerName(consumerName)
                                        isConsumerSelectionExpanded = false
                                    },
                                    leadingIcon = {
                                        if (statsModel.consumerName == consumerName) {
                                            Icon(
                                                Icons.Filled.Check,
                                                contentDescription = "Check",
                                                modifier = Modifier.size(ButtonDefaults.IconSize)
                                            )
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (!statsModel.areThereAnyIngestions) {
            EmptyScreenDisclaimer(
                title = "Nothing to show yet",
                description = "Start logging your ingestions to see an overview of your consumption pattern."
            )
        } else {
            Column(modifier = Modifier.padding(padding)) {
                TabRow(
                    selectedTabIndex = statsModel.selectedOption.tabIndex,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    TimePickerOption.values().forEachIndexed { index, option ->
                        Tab(
                            text = { Text(option.displayText) },
                            selected = statsModel.selectedOption.tabIndex == index,
                            onClick = { onTapOption(option) }
                        )
                    }
                }
                if (statsModel.statItems.isNotEmpty()) {
                    val isDarkTheme = isSystemInDarkTheme()
                    Column {
                        Text(
                            text = "Experiences since ${statsModel.startDateText}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                        )
                        Text(
                            text = "Substance counted once per experience",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(
                                start = 10.dp,
                                bottom = 10.dp
                            )
                        )
                        BarChart(
                            buckets = statsModel.chartBuckets,
                            startDateText = statsModel.startDateText
                        )
                        HorizontalDivider()
                        LazyColumn {
                            items(statsModel.statItems) { subStat ->
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(intrinsicSize = IntrinsicSize.Min)
                                            .clickable {
                                                navigateToSubstanceCompanion(
                                                    subStat.substanceName,
                                                    statsModel.consumerName
                                                )
                                            }
                                            .padding(
                                                horizontal = horizontalPadding,
                                                vertical = 5.dp
                                            )
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(3.dp),
                                            color = subStat.color.getComposeColor(
                                                isDarkTheme
                                            ),
                                            modifier = Modifier
                                                .width(11.dp)
                                                .fillMaxHeight()
                                        ) {}
                                        Column {
                                            Text(
                                                text = subStat.substanceName,
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            val addOn =
                                                if (subStat.experienceCount == 1) " experience" else " experiences"
                                            Text(
                                                text = subStat.experienceCount.toString() + addOn,
                                            )
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        Column(horizontalAlignment = Alignment.End) {
                                            val cumulativeDose = subStat.totalDose
                                            if (cumulativeDose != null) {
                                                if (cumulativeDose.isEstimate) {
                                                    if (cumulativeDose.estimatedDoseStandardDeviation != null) {
                                                        Text(text = "total ${cumulativeDose.dose.toReadableString()}±${cumulativeDose.estimatedDoseStandardDeviation.toReadableString()} ${cumulativeDose.units}")
                                                    } else {
                                                        Text(text = "total ~${cumulativeDose.dose.toReadableString()} ${cumulativeDose.units}")
                                                    }
                                                } else {
                                                    Text(text = "total ${cumulativeDose.dose.toReadableString()} ${cumulativeDose.units}")

                                                }
                                            } else {
                                                Text(text = "total dose unknown")
                                            }
                                            subStat.routeCounts.forEach {
                                                Text(
                                                    text = "${it.administrationRoute.displayText.lowercase()} ${it.count}x ",
                                                )
                                            }
                                        }

                                    }
                                    HorizontalDivider()
                                }
                            }
                        }
                    }
                } else {
                    EmptyScreenDisclaimer(
                        title = "No ingestions since ${statsModel.selectedOption.longDisplayText}",
                        description = "Choose a longer duration range to see statistics."
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyScreenDisclaimer(title: String, description: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = description,
                textAlign = TextAlign.Center
            )
        }
    }
}