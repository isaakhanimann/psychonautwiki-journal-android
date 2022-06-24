package com.example.healthassistant.ui.experiences.experience

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
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
        deleteIngestion = {}
    )
}


@Composable
fun IngestionRow(
    ingestion: Ingestion,
    deleteIngestion: () -> Unit,
    modifier: Modifier = Modifier,
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
                    Text(text = ingestion.substanceName, style = MaterialTheme.typography.h6)
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
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
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
                Text(text = timeString)
                var isExpanded by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    IconButton(onClick = { isExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
                    }
                    DropdownMenu(
                        expanded = isExpanded,
                        onDismissRequest = { isExpanded = false }
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                deleteIngestion()
                                isExpanded = false
                            }
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}