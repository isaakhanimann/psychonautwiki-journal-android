package com.isaakhanimann.healthassistant.ui.ingestions.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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


@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    navigateToSubstanceCompanion: (substanceName: String) -> Unit
) {
    StatsScreen(
        substancesLastUsed = viewModel.substanceStats.collectAsState().value,
        navigateToSubstanceCompanion = navigateToSubstanceCompanion,
        selectedOption = viewModel.optionFlow.collectAsState().value,
        onTapOption = viewModel::onTapOption
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
        onTapOption = {}
    )
}

@Composable
fun StatsScreen(
    substancesLastUsed: List<SubstanceStat>,
    navigateToSubstanceCompanion: (substanceName: String) -> Unit,
    selectedOption: TimePickerOption,
    onTapOption: (option: TimePickerOption) -> Unit
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
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Text tab selected",
                    style = MaterialTheme.typography.body1
                )
            }

//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//            ) {
//                item {
//                    Row {
//                        TimePickerOption.values().forEach { thisOption ->
//                            val isChecked = thisOption == selectedOption
//                            IconToggleButton(checked = isChecked, onCheckedChange = { onTapOption(thisOption) }) {
////                                val tint by animateColorAsState(if (checked) Color(0xFFEC407A) else Color(0xFFB0BEC5))
//                                Text(text = thisOption.displayText)
//                            }
//                        }
//                    }
//                }
//                items(substancesLastUsed.size) { i ->
//                    val subStat = substancesLastUsed[i]
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(10.dp),
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                navigateToSubstanceCompanion(subStat.substanceName)
//                            }
//                            .padding(horizontal = 10.dp, vertical = 5.dp)
//                    ) {
//                        val isDarkTheme = isSystemInDarkTheme()
//                        Surface(
//                            shape = CircleShape,
//                            color = subStat.color.getComposeColor(isDarkTheme),
//                            modifier = Modifier.size(25.dp)
//                        ) {}
//                        Text(text = subStat.substanceName, style = MaterialTheme.typography.h6)
//                        Spacer(modifier = Modifier.weight(1f))
//                        Text(
//                            text = subStat.ingestionCount.toString(),
//                            style = MaterialTheme.typography.subtitle2
//                        )
//                    }
//                    if (i < substancesLastUsed.size) {
//                        Divider()
//                    }
//                }
//            }
        }
    }
}