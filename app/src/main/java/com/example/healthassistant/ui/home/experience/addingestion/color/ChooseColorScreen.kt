package com.example.healthassistant.ui.home.experience.addingestion.color

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.ui.main.routers.navigateToChooseTime


@Composable
fun ChooseColorScreen(
    navController: NavController,
    viewModel: ChooseColorViewModel = hiltViewModel()
) {
    ChooseColorScreenContent(
        lastUsedColor = viewModel.lastUsedColorForSubstance,
        navigateToNext = {
            navController.navigateToChooseTime(
                substanceName = viewModel.substanceName,
                administrationRoute = viewModel.administrationRoute,
                units = viewModel.units,
                isEstimate = viewModel.isEstimate,
                color = it,
                dose = viewModel.dose,
                experienceId = viewModel.experienceId
            )
        }
    )
}

val allColors = listOf(
    Color.Blue,
    Color.Cyan,
    Color.Gray,
    Color.DarkGray,
    Color.Green,
    Color.Magenta,
    Color.Yellow,
    Color.Red,
    Color.Blue,
    Color.Cyan,
    Color.Gray,
    Color.DarkGray,
    Color.Blue,
    Color.Cyan,
    Color.Gray,
    Color.DarkGray,
    Color.Green,
    Color.Magenta,
)

@Preview
@Composable
fun ChooseColorPreview() {
    ChooseColorScreenContent(
        lastUsedColor = Color.Red,
        navigateToNext = {}
    )
}

@Composable
fun ChooseColorScreenContent(
    lastUsedColor: Color?,
    navigateToNext: (Color) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Color") }) }
    ) {
        val spacing = 6
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.dp)
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(spacing.dp)
            ) {
                val otherColors = allColors.filter { it != lastUsedColor }
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
                                backgroundColor = color
                            ) {}
                        }
                    }
                }
            }
            lastUsedColor?.let { color ->
                Card(
                    modifier = Modifier
                        .clickable {
                            navigateToNext(color)
                        }
                        .fillMaxWidth()
                        .weight(1f),
                    backgroundColor = color
                ) {}
            }
        }
    }
}
