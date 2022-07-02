package com.example.healthassistant.ui.experiences.experience

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.ui.ingestions.DotRow
import com.example.healthassistant.ui.ingestions.IngestionCircle
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun IngestionRowPreview(@PreviewParameter(IngestionRowPreviewProvider::class) ingestionElement: OneExperienceViewModel.IngestionElement) {
    IngestionRow(
        ingestionElement = ingestionElement,
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
            IngestionCircle(
                substanceColor = ingestionWithCompanion.substanceCompanion!!.color,
                sentiment = ingestion.sentiment
            )
            Column {
                Text(text = ingestion.substanceName, style = MaterialTheme.typography.h6)
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                        ingestion.dose?.also {
                            Text(
                                text = "${if (ingestion.isDoseAnEstimate) "~" else ""}${it.toReadableString()} ${ingestion.units} ${ingestion.administrationRoute.displayText}",
                                style = MaterialTheme.typography.subtitle1
                            )
                        } ?: run {
                            Text(
                                text = "Unknown Dose ${ingestion.administrationRoute.displayText}",
                                style = MaterialTheme.typography.subtitle1
                            )
                        }
                        if (!ingestion.notes.isNullOrBlank()) {
                            Icon(
                                Icons.Outlined.StickyNote2,
                                contentDescription = "Ingestion has note"
                            )
                        }
                    }
                }
                val doseClass = ingestionElement.doseClass
                if (doseClass != null) {
                    DotRow(doseClass = doseClass)
                }
            }
        }
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
        Text(text = timeString)
    }
}