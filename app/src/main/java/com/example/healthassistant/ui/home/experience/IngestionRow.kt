package com.example.healthassistant.ui.home.experience

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.ui.previewproviders.IngestionPreviewProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun IngestionRow(
    @PreviewParameter(IngestionPreviewProvider::class) ingestion: Ingestion,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    Card(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = ingestion.color.getComposeColor(isDarkTheme),
                    modifier = Modifier.size(25.dp)
                ) {}
                Column {
                    Text(text = ingestion.substanceName, style = MaterialTheme.typography.subtitle1)
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        ingestion.dose?.let {
                            Text(text = "${if (ingestion.isDoseAnEstimate) "~" else ""}${it.toReadableString()} ${ingestion.units} ${ingestion.administrationRoute.displayText}")
                        } ?: run {
                            Text(text = "Unknown Dose ${ingestion.administrationRoute.displayText}")
                        }
                    }
                }
            }
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
            Text(text = timeString)
        }
    }
}