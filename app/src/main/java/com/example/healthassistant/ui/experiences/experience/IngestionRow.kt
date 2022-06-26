package com.example.healthassistant.ui.experiences.experience

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
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
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.ui.previewproviders.IngestionPreviewProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun IngestionRowPreview(@PreviewParameter(IngestionPreviewProvider::class) ingestion: Ingestion) {
    IngestionRow(
        ingestion = ingestion,
        navigateToIngestionScreen = {}
    )
}


@Composable
fun IngestionRow(
    ingestion: Ingestion,
    navigateToIngestionScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDarkTheme = isSystemInDarkTheme()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = navigateToIngestionScreen)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // TODO
//                Surface(
//                    shape = CircleShape,
//                    color = ingestion.color.getComposeColor(isDarkTheme),
//                    modifier = Modifier.size(25.dp)
//                ) {}
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
                }
            }
            val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
            Text(text = timeString)
        }
    }
}