package com.example.healthassistant.ui.addingestion.dose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.DoseClass
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.ui.search.substance.roa.dose.RoaDosePreviewProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString


@Preview
@Composable
fun CurrentDoseClassInfoPreview(@PreviewParameter(RoaDosePreviewProvider::class) roaDose: RoaDose) {
    CurrentDoseClassInfo(currentDoseClass = DoseClass.LIGHT, roaDose = roaDose)
}

@Composable
fun CurrentDoseClassInfo(currentDoseClass: DoseClass?, roaDose: RoaDose) {
    val height = 50.dp
    if (currentDoseClass != null) {
        val doseColor = currentDoseClass.getComposeColor(isSystemInDarkTheme())
        var isShowingDoseClassDialog by remember { mutableStateOf(false) }
        TextButton(
            onClick = {
                isShowingDoseClassDialog = true
            },
            modifier = Modifier.height(height)
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = "Info",
                modifier = Modifier.size(25.dp),
                tint = doseColor
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            val text = when (currentDoseClass) {
                DoseClass.THRESHOLD -> "threshold: ${roaDose.threshold?.toReadableString()}${roaDose.units ?: ""}"
                DoseClass.LIGHT -> "light: ${roaDose.light?.min?.toReadableString()}-${roaDose.light?.max?.toReadableString()}${roaDose.units ?: ""}"
                DoseClass.COMMON -> "common: ${roaDose.common?.min?.toReadableString()}-${roaDose.common?.max?.toReadableString()}${roaDose.units ?: ""}"
                DoseClass.STRONG -> "strong: ${roaDose.strong?.min?.toReadableString()}-${roaDose.strong?.max?.toReadableString()}${roaDose.units ?: ""}"
                DoseClass.HEAVY -> "heavy: ${roaDose.heavy?.toReadableString()}${roaDose.units ?: ""}-.."
            }
            Text(
                text = text,
                color = doseColor,
                style = MaterialTheme.typography.h5
            )
        }
        if (isShowingDoseClassDialog) {
            AlertDialog(
                onDismissRequest = { isShowingDoseClassDialog = false },
                title = {
                    Text(
                        text = "${currentDoseClass.name} DOSAGE",
                        style = MaterialTheme.typography.h6,
                    )
                },
                text = {
                    Text(
                        text = currentDoseClass.description,
                        style = if (currentDoseClass==DoseClass.HEAVY) MaterialTheme.typography.caption else MaterialTheme.typography.body1
                    )
                },
                buttons = {
                    Row(
                        modifier = Modifier.padding(all = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { isShowingDoseClassDialog = false }
                        ) {
                            Text("Dismiss")
                        }
                    }
                },
            )
        }
    } else {
        Text(
            text = "",
            modifier = Modifier.height(height)
        )
    }
}