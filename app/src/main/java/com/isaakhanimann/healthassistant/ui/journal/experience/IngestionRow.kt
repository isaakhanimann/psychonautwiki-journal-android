package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import com.isaakhanimann.healthassistant.ui.utils.getStringOfPattern

@Preview
@Composable
fun IngestionRowPreview(@PreviewParameter(IngestionRowPreviewProvider::class) ingestionElement: IngestionElement) {
    IngestionRow(
        ingestionElement = ingestionElement,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun IngestionRow(
    ingestionElement: IngestionElement,
    modifier: Modifier = Modifier,
) {
    val ingestionWithCompanion = ingestionElement.ingestionWithCompanion
    val ingestion = ingestionWithCompanion.ingestion
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        val isDarkTheme = isSystemInDarkTheme()
        Surface(
            shape = CircleShape,
            color = ingestionWithCompanion.substanceCompanion!!.color.getComposeColor(
                isDarkTheme
            ),
            modifier = Modifier
                .size(25.dp)
        ) {}
        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = ingestion.substanceName, style = MaterialTheme.typography.h6)
                    val timeString = ingestion.time.getStringOfPattern("EEE HH:mm")
                    Text(text = timeString)
                }
                Column(horizontalAlignment = Alignment.End) {
                    ingestion.dose?.also {
                        val isEstimateText = if (ingestion.isDoseAnEstimate) "~" else ""
                        val doseText = it.toReadableString()
                        Text(text = "${ingestion.administrationRoute.displayText} $isEstimateText$doseText ${ingestion.units}")
                    } ?: run {
                        Text(text = "${ingestion.administrationRoute.displayText} Unknown Dose")
                    }
                    val doseClass = ingestionElement.doseClass
                    if (doseClass != null) {
                        DotRow(doseClass = doseClass)
                    }
                }
            }
            val note = ingestion.notes
            if (!note.isNullOrBlank()) {
                Text(text = note, style = MaterialTheme.typography.caption)
            }
        }
    }
}

@Composable
fun DotRow(doseClass: DoseClass) {
    val dotSize = 10.dp
    val horizontalPadding = 1.dp
    Row(modifier = Modifier.padding(vertical = 3.dp)) {
        List(doseClass.numDots, init = { it }).forEach { _ ->
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.onBackground)
            )
        }
        val numEmpty = DoseClass.values().size - doseClass.numDots
        List(numEmpty, init = { it }).forEach { _ ->
            Box(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .size(dotSize)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colors.onBackground, CircleShape)
            )
        }
    }
}