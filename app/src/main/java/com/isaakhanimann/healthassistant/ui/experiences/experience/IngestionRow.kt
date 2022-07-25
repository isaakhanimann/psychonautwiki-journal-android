package com.isaakhanimann.healthassistant.ui.experiences.experience

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.ingestions.DotRow
import com.isaakhanimann.healthassistant.ui.ingestions.IngestionCircle
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
            IngestionCircle(
                substanceColor = ingestionWithCompanion.substanceCompanion!!.color,
                sentiment = ingestion.sentiment
            )
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