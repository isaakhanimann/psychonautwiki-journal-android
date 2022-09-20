package com.isaakhanimann.healthassistant.ui.ingestions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.StickyNote2
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceColor
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.ui.experiences.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.roa.toReadableString
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    viewModel: IngestionsViewModel = hiltViewModel()
) {
    IngestionsScreen(
        navigateToIngestion = navigateToIngestion,
        navigateToAddIngestion = navigateToAddIngestion,
        groupedIngestions = viewModel.ingestionsGrouped.collectAsState().value,
    )
}

@Preview
@Composable
fun IngestionsScreenPreview(
    @PreviewParameter(
        IngestionsScreenPreviewProvider::class,
    ) groupedIngestions: Map<String, List<IngestionsViewModel.IngestionElement>>
) {
    IngestionsScreen(
        navigateToIngestion = {},
        navigateToAddIngestion = {},
        groupedIngestions = groupedIngestions
    )
}

@Composable
fun IngestionsScreen(
    navigateToIngestion: (ingestionId: Int) -> Unit,
    navigateToAddIngestion: () -> Unit,
    groupedIngestions: Map<String, List<IngestionsViewModel.IngestionElement>>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ingestions") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToAddIngestion,
                icon = {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                },
                text = { Text("Ingestion") },
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
                        val ingestionElement = ingestionsInYear[i]
                        IngestionRowInIngestionsScreen(
                            ingestionElement = ingestionElement,
                            navigateToIngestion = {
                                navigateToIngestion(ingestionElement.ingestionWithCompanion.ingestion.id)
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
    ingestionElement: IngestionsViewModel.IngestionElement,
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
            val ingestionWithCompanion = ingestionElement.ingestionWithCompanion
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
                if (ingestionElement.doseClass != null) {
                    DotRow(doseClass = ingestionElement.doseClass)
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