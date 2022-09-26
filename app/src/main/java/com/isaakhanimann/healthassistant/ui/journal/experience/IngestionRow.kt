package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun IngestionRowPreview(@PreviewParameter(IngestionRowPreviewProvider::class) ingestionElement: OneExperienceViewModel.IngestionElement) {
    IngestionRow(
        ingestionElement = ingestionElement,
        modifier = Modifier.fillMaxWidth()
    )
}


@Composable
fun IngestionRow(
    ingestionElement: OneExperienceViewModel.IngestionElement,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val ingestionWithCompanion = ingestionElement.ingestionWithCompanion
        val ingestion = ingestionWithCompanion.ingestion
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val isDarkTheme = isSystemInDarkTheme()
            Surface(
                shape = CircleShape,
                color = ingestionWithCompanion.substanceCompanion!!.color.getComposeColor(
                    isDarkTheme
                ),
                modifier = Modifier
                    .size(40.dp)
            ) {}
            Column(modifier = Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(text = ingestion.substanceName, style = MaterialTheme.typography.h6)
                    if (!ingestion.notes.isNullOrBlank()) {
                        Icon(
                            Icons.Outlined.StickyNote2,
                            contentDescription = "Ingestion has note"
                        )
                    }
                }
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
                Text(text = timeString)
            }
            Column(horizontalAlignment = Alignment.End) {
                ingestion.dose?.also {
                    val isEstimateText = if (ingestion.isDoseAnEstimate) "~" else ""
                    val doseText = it.toReadableString()
                    Text(
                        text = "${ingestion.administrationRoute.displayText} $isEstimateText$doseText ${ingestion.units}",
                        style = MaterialTheme.typography.subtitle1
                    )
                } ?: run {
                    Text(
                        text = "${ingestion.administrationRoute.displayText} Unknown Dose",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                val doseClass = ingestionElement.doseClass
                if (doseClass != null) {
                    DotRow(doseClass = doseClass)
                }
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