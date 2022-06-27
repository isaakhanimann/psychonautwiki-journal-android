package com.example.healthassistant.ui.addingestion.time.colorpicker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun ColorPicker(
    viewModel: ColorPickerViewModel = hiltViewModel(),
    selectedColor: SubstanceColor,
    onChangeOfColor: (SubstanceColor) -> Unit
) {
    ColorPicker(
        selectedColor = selectedColor,
        onChangeOfColor = onChangeOfColor,
        alreadyUsedColors = viewModel.alreadyUsedColorsFlow.collectAsState().value,
        otherColors = viewModel.otherColorsFlow.collectAsState().value
    )
}

@Preview
@Composable
fun ColorPickerPreview() {
    val alreadyUsedColors = listOf(SubstanceColor.BLUE, SubstanceColor.PINK)
    val otherColors = SubstanceColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    ColorPicker(
        selectedColor = SubstanceColor.BLUE,
        onChangeOfColor = {},
        alreadyUsedColors,
        otherColors
    )
}

@Composable
fun ColorPicker(
    selectedColor: SubstanceColor,
    onChangeOfColor: (SubstanceColor) -> Unit,
    alreadyUsedColors: List<SubstanceColor>,
    otherColors: List<SubstanceColor>
) {
    val isDarkTheme = isSystemInDarkTheme()
    var isColorPickerVisible by remember {
        mutableStateOf(false)
    }
    Surface(
        shape = CircleShape,
        color = selectedColor.getComposeColor(isDarkTheme),
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .clickable(onClick = { isColorPickerVisible = true })
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit Color",
            modifier = Modifier.padding(20.dp),
        )
    }
    if (isColorPickerVisible) {
        ColorDialog(
            dismiss = { isColorPickerVisible = false },
            onChangeOfColor = onChangeOfColor,
            alreadyUsedColors = alreadyUsedColors,
            otherColors = otherColors
        )
    }
}

@Preview
@Composable
fun ColorDialogPreview() {
    val alreadyUsedColors = listOf(SubstanceColor.BLUE, SubstanceColor.PINK)
    val otherColors = SubstanceColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    ColorDialog(
        dismiss = {},
        onChangeOfColor = {},
        alreadyUsedColors = alreadyUsedColors,
        otherColors = otherColors
    )
}

@Composable
fun ColorDialog(
    dismiss: () -> Unit,
    onChangeOfColor: (SubstanceColor) -> Unit,
    alreadyUsedColors: List<SubstanceColor>,
    otherColors: List<SubstanceColor>
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(text = "Pick a Color", style = MaterialTheme.typography.h6)
        },
        text = {
            Column {
                Text(text = "Not Yet Used", style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.height(2.dp))
                CircleColorButtons(colors = otherColors) {
                    onChangeOfColor(it)
                    dismiss()
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Already Used", style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.height(2.dp))
                CircleColorButtons(colors = alreadyUsedColors) {
                    onChangeOfColor(it)
                    dismiss()
                }
            }
        },
        buttons = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = dismiss
            ) {
                Text("Cancel", textAlign = TextAlign.Center)
            }
        },
    )
}

@Composable
fun CircleColorButtons(
    colors: List<SubstanceColor>,
    onTapOnColor: (SubstanceColor) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    val spacing = 4.dp
    FlowRow(
        mainAxisSpacing = spacing,
        crossAxisSpacing = spacing
    ) {
        colors.forEach { color ->
            Surface(
                shape = CircleShape,
                color = color.getComposeColor(isDarkTheme),
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = { onTapOnColor(color) }
                    )
            ) {}
        }
    }

}