package com.example.healthassistant.ui.journal.experience.addingestion.color

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.IngestionColor


@Composable
fun ChooseColorScreen(
    navigateToChooseTime: (color: IngestionColor) -> Unit,
    viewModel: ChooseColorViewModel = hiltViewModel()
) {
    ChooseColorScreenContent(
        lastUsedColor = viewModel.lastUsedColorForSubstance,
        navigateToNext = {
            navigateToChooseTime(it)
        }
    )
}

@Preview
@Composable
fun ChooseColorPreview() {
    ChooseColorScreenContent(
        lastUsedColor = IngestionColor.INDIGO,
        navigateToNext = {}
    )
}

@Composable
fun ChooseColorScreenContent(
    lastUsedColor: IngestionColor?,
    navigateToNext: (IngestionColor) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Color") }) }
    ) {
        val spacing = 6
        val isDarkTheme = isSystemInDarkTheme()
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.dp)
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.dp)
            ) {
                val otherColors = IngestionColor.values().filter { it != lastUsedColor }
                val otherColorsChunked = otherColors.chunked(3)
                otherColorsChunked.forEach { otherColorChunk ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
                    ) {
                        otherColorChunk.forEach { color ->
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        navigateToNext(color)
                                    }
                                    .fillMaxHeight()
                                    .weight(1f),
                                backgroundColor = color.getComposeColor(isDarkTheme)
                            ) {}
                        }
                    }
                }
            }
            lastUsedColor?.also { color ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navigateToNext(color)
                        }
                        .fillMaxWidth()
                        .weight(1f),
                    backgroundColor = color.getComposeColor(isDarkTheme)
                ) {}
            }
        }
    }
}
