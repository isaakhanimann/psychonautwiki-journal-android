package com.example.healthassistant.ui.ingestions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Sentiment
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import com.example.healthassistant.ui.experiences.SectionTitle
import com.example.healthassistant.ui.previewproviders.IngestionsScreenPreviewProvider
import com.example.healthassistant.ui.search.substance.roa.toReadableString
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    viewModel: IngestionsViewModel = hiltViewModel()
) {
    IngestionsScreen(
        navigateToIngestion = navigateToIngestion,
        groupedIngestions = viewModel.ingestionsGrouped.collectAsState().value,
        filterOptions = viewModel.filterOptions.collectAsState().value,
        numberOfActiveFilters = viewModel.numberOfActiveFilters.collectAsState().value
    )
}

@Preview
@Composable
fun IngestionsScreenPreview(
    @PreviewParameter(
        IngestionsScreenPreviewProvider::class,
    ) groupedIngestions: Map<String, List<IngestionWithCompanion>>
) {
    IngestionsScreen(
        navigateToIngestion = {},
        groupedIngestions = groupedIngestions,
        filterOptions = listOf(),
        numberOfActiveFilters = 1
    )
}

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    groupedIngestions: Map<String, List<IngestionWithCompanion>>,
    filterOptions: List<IngestionsViewModel.FilterOption>,
    numberOfActiveFilters: Int,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ingestions") },
                actions = {
                    var isExpanded by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopEnd)
                    ) {
                        IconButton(
                            onClick = { isExpanded = true },
                        ) {
                            BadgedBox(badge = {
                                if (numberOfActiveFilters != 0) {
                                    Badge { Text(numberOfActiveFilters.toString()) }
                                }
                            }) {
                                Icon(
                                    Icons.Filled.FilterList,
                                    contentDescription = "Filter"
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false }
                        ) {
                            filterOptions.forEach { filterOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        filterOption.onTap()
                                    },
                                    enabled = filterOption.isEnabled
                                ) {
                                    if (filterOption.hasCheck) {
                                        Icon(
                                            Icons.Filled.Check,
                                            contentDescription = "Check",
                                            modifier = Modifier.size(ButtonDefaults.IconSize)
                                        )
                                    } else {
                                        Spacer(Modifier.size(ButtonDefaults.IconSize))
                                    }
                                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                                    Text(filterOption.name)
                                }
                            }
                        }
                    }
                }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                groupedIngestions.forEach { (year, ingestionsInYear) ->
                    item {
                        SectionTitle(title = year)
                    }
                    items(ingestionsInYear.size) { i ->
                        val ingestionWithCompanion = ingestionsInYear[i]
                        IngestionRowInIngestionsScreen(
                            ingestionWithCompanion = ingestionWithCompanion,
                            navigateToIngestion = {
                                navigateToIngestion(ingestionWithCompanion.ingestion.id)
                            }
                        )
                        if (i < ingestionsInYear.size) {
                            Divider()
                        }
                    }
                }
            }
            if (groupedIngestions.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(text = "No Ingestions Yet")
                }
            }
        }
    }
}


@Composable
fun IngestionRowInIngestionsScreen(
    ingestionWithCompanion: IngestionWithCompanion,
    navigateToIngestion: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = navigateToIngestion)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val ingestion = ingestionWithCompanion.ingestion
            IngestionCircle(
                substanceColor = ingestionWithCompanion.substanceCompanion!!.color,
                sentiment = ingestion.sentiment
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingestion.substanceName,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
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
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val dateString = dateFormatter.format(ingestion.time) ?: "Unknown Date"
                val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
                val timeString = timeFormatter.format(ingestion.time) ?: "Unknown Time"
                Text(text = dateString, style = MaterialTheme.typography.body1)
                Text(text = timeString, style = MaterialTheme.typography.body1)
            }

        }
    }
}

@Composable
fun IngestionCircle(
    substanceColor: SubstanceColor,
    sentiment: Sentiment?
) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        shape = CircleShape,
        color = substanceColor.getComposeColor(isDarkTheme),
        modifier = Modifier
            .size(40.dp)
    ) {
        if (sentiment != null) {
            Icon(
                imageVector = sentiment.icon,
                contentDescription = sentiment.description,
                modifier = Modifier.padding(5.dp),
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}