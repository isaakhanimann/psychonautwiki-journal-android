/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor

@Preview
@Composable
fun ColorPickerPreview() {
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
    val otherColors = AdaptiveColor.values().filter { color ->
        !alreadyUsedColors.contains(color)
    }
    ColorPicker(
        selectedColor = AdaptiveColor.BLUE,
        onChangeOfColor = {},
        alreadyUsedColors,
        otherColors
    )
}

@Composable
fun ColorPicker(
    selectedColor: AdaptiveColor,
    onChangeOfColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>
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
    val alreadyUsedColors = listOf(AdaptiveColor.BLUE, AdaptiveColor.PINK)
    val otherColors = AdaptiveColor.values().filter { color ->
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
    onChangeOfColor: (AdaptiveColor) -> Unit,
    alreadyUsedColors: List<AdaptiveColor>,
    otherColors: List<AdaptiveColor>
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(text = "Pick a Color", style = MaterialTheme.typography.titleLarge)
        },
        text = {
            Column {
                if (otherColors.isEmpty()) {
                    Text(text = "No Unused Colors")
                } else {
                    Text(text = "Not Yet Used")
                    Spacer(modifier = Modifier.height(2.dp))
                    CircleColorButtons(colors = otherColors) {
                        onChangeOfColor(it)
                        dismiss()
                    }
                }
                if (alreadyUsedColors.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "Already Used")
                    Spacer(modifier = Modifier.height(2.dp))
                    CircleColorButtons(colors = alreadyUsedColors) {
                        onChangeOfColor(it)
                        dismiss()
                    }
                }
            }
        },
        confirmButton = {
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
    colors: List<AdaptiveColor>,
    onTapOnColor: (AdaptiveColor) -> Unit
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