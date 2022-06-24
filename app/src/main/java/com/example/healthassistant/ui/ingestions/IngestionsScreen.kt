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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.ui.experiences.SectionTitle
import com.example.healthassistant.ui.previewproviders.IngestionPreviewProvider
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
        numberOfActiveFilters = viewModel.numberOfActiveFilters
    )
}

@Preview
@Composable
fun IngestionsScreenPreview() {
    IngestionsScreen(
        navigateToIngestion = {},
        groupedIngestions = emptyMap(),
        filterOptions = listOf(),
        numberOfActiveFilters = 1
    )
}

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    groupedIngestions: Map<String, List<Ingestion>>,
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
                        val ingestion = ingestionsInYear[i]
                        IngestionRowInIngestionsScreen(
                            ingestion = ingestion,
                            navigateToIngestion = {
                                navigateToIngestion(ingestion.id)
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

@Preview
@Composable
fun IngestionRowInIngestionsScreenPreview(@PreviewParameter(IngestionPreviewProvider::class) ingestion: Ingestion) {
    IngestionRowInIngestionsScreen(
        ingestion = ingestion,
        navigateToIngestion = {}
    )
}


@Composable
fun IngestionRowInIngestionsScreen(
    ingestion: Ingestion,
    navigateToIngestion: () -> Unit,
) {
    val isDarkTheme = isSystemInDarkTheme()
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
            Surface(
                shape = CircleShape,
                color = ingestion.color.getComposeColor(isDarkTheme),
                modifier = Modifier.size(25.dp)
            ) {}
            Spacer(modifier = Modifier.width(10.dp))
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
            Spacer(modifier = Modifier.weight(1f))
            val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            val timeString = formatter.format(ingestion.time) ?: "Unknown Time"
            Text(text = timeString, style = MaterialTheme.typography.body1)
        }
    }
}