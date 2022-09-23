package com.isaakhanimann.healthassistant.ui.stats

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
        navigateToSubstanceCompanion = navigateToSubstanceCompanion,
        onTapOption = viewModel::onTapOption,
        statsModel = viewModel.statsModelFlow.collectAsState().value
    )
}

@Preview
@Composable
fun StatsPreview(
    @PreviewParameter(
        StatsPreviewProvider::class,
    ) statsModel: StatsModel
) {
    StatsScreen(
        navigateToSubstanceCompanion = {},
        onTapOption = {},
        statsModel = statsModel
    )
}

@Composable
fun StatsScreen(
    navigateToSubstanceCompanion: (substanceName: String) -> Unit,
    onTapOption: (option: TimePickerOption) -> Unit,
    statsModel: StatsModel?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Statistics") },
                elevation = 0.dp
            )
        }
    ) {
        if (statsModel?.substanceStats?.isEmpty() != false) {
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
                TabRow(selectedTabIndex = statsModel.selectedOption.tabIndex) {
                    TimePickerOption.values().forEachIndexed { index, option ->
                        Tab(
                            text = { Text(option.displayText) },
                            selected = statsModel.selectedOption.tabIndex == index,
                            onClick = { onTapOption(option) }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val style = MaterialTheme.typography.h5
                    Text(
                        text = statsModel.startDateText,
                        style = style,
                    )
                    ArrowRight(
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .padding(horizontal = 8.dp),
                        strokeWidth = 4f
                    )
                    Text(
                        text = "Now",
                        style = style,
                    )
                }
                Text(
                    text = "Ingestion Counts",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
                )
                BarChart(buckets = statsModel.chartBuckets)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 5.dp, vertical = 5.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val style = MaterialTheme.typography.caption
                        Text(
                            text = statsModel.startDateText,
                            style = style,
                        )
                        Text(
                            text = "Now",
                            style = style,
                        )
                    }
                }
                Divider()
                val isDarkTheme = isSystemInDarkTheme()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(statsModel.substanceStats.size) { i ->
                        val subStat = statsModel.substanceStats[i]
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
                            Surface(
                                shape = CircleShape,
                                color = subStat.color.getComposeColor(isDarkTheme),
                                modifier = Modifier.size(25.dp)
                            ) {}
                            Column {
                                Text(
                                    text = subStat.substanceName,
                                    style = MaterialTheme.typography.subtitle1
                                )
                                subStat.routeCounts.forEach {
                                    Text(
                                        text = "${it.count}x ${it.administrationRoute.displayText}",
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            val cumulativeDose = subStat.cumulativeDose
                            if (cumulativeDose != null) {
                                Text(text = "total ${if (cumulativeDose.isEstimate) "~" else ""}${cumulativeDose.dose.toReadableString()} ${cumulativeDose.units}")
                            } else {
                                Text(text = "total dose unknown")
                            }

                        }
                        if (i < statsModel.substanceStats.size) {
                            Divider()
                        }
                    }
                }
            }


        }
    }
}