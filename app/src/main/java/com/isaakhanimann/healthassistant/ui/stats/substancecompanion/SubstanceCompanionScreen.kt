package com.isaakhanimann.healthassistant.ui.stats.substancecompanion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.ui.addingestion.time.ColorPicker

@Composable
fun SubstanceCompanionScreen(
    viewModel: SubstanceCompanionViewModel = hiltViewModel()
) {
    viewModel.thisCompanionFlow.collectAsState().value?.also { companion ->
        SubstanceCompanionScreen(
            substanceCompanion = companion,
            onChangeColor = { viewModel.updateColor(it) },
            alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
            otherColors = viewModel.otherColorsFlow.collectAsState().value
        )
    }
}

@Preview
@Composable
fun SubstanceCompanionPreview(@PreviewParameter(SubstanceCompanionPreviewProvider::class) companion: SubstanceCompanion) {
    val alreadyUsedColors = listOf(SubstanceColor.BLUE, SubstanceColor.PINK)
    val otherColors = SubstanceColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    SubstanceCompanionScreen(
        substanceCompanion = companion,
        onChangeColor = {},
        alreadyUsedColors = alreadyUsedColors,
        otherColors = otherColors
    )
}

@Composable
fun SubstanceCompanionScreen(
    substanceCompanion: SubstanceCompanion,
    onChangeColor: (SubstanceColor) -> Unit,
    alreadyUsedColors: List<SubstanceColor>,
    otherColors: List<SubstanceColor>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = substanceCompanion.substanceName) },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ColorPicker(
                selectedColor = substanceCompanion.color,
                onChangeOfColor = onChangeColor,
                alreadyUsedColors = alreadyUsedColors,
                otherColors = otherColors
            )
        }
    }
}