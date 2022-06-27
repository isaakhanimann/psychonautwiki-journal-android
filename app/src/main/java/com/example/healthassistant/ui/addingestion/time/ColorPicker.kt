package com.example.healthassistant.ui.addingestion.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.google.accompanist.flowlayout.FlowRow

@Preview
@Composable
fun ColorPickerPreview() {
    ColorPicker(selectedColor = SubstanceColor.BLUE, onChangeOfColor = {})
}

@Composable
fun ColorPicker(
    selectedColor: SubstanceColor,
    onChangeOfColor: (SubstanceColor) -> Unit
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
            onChangeOfColor = onChangeOfColor
        )
    }
}

@Preview
@Composable
fun ColorDialogPreview() {
    ColorDialog(
        dismiss = {},
        onChangeOfColor = {}
    )
}

@Composable
fun ColorDialog(
    dismiss: () -> Unit,
    onChangeOfColor: (SubstanceColor) -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(text = "Pick a Color")
        },
        text = {
            FlowRow {
                SubstanceColor.values().forEach { color ->
                    Surface(
                        shape = CircleShape,
                        color = color.getComposeColor(isDarkTheme),
                        modifier = Modifier
                            .padding(2.dp)
                            .size(40.dp)
                            .clickable(
                                onClick = {
                                    onChangeOfColor(color)
                                    dismiss()
                                }
                            )
                    ) {}
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