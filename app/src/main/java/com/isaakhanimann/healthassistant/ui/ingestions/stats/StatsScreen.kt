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


@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    navigateToSubstanceCompanion: (substanceName: String) -> Unit
) {
    StatsScreen(
        substancesLastUsed = viewModel.substanceStats.collectAsState().value,
        navigateToSubstanceCompanion = navigateToSubstanceCompanion
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
        navigateToSubstanceCompanion = {}
    )
}

@Composable
fun StatsScreen(
    substancesLastUsed: List<SubstanceStat>,
    navigateToSubstanceCompanion: (substanceName: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Statistics") }
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
                            Text(text = "Last used ${subStat.lastUsedText} ago")
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = subStat.ingestionCount.toString(),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                    if (i < substancesLastUsed.size) {
                        Divider()
                    }
                }
            }
        }
    }
}