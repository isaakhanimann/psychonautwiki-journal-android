package com.example.healthassistant.ui.home.experience.timeline.ingestion

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.IngestionTimelineProvider


@Composable
fun IngestionTimeline(
    ingestion: Ingestion,
    modifier: Modifier
) {
    val viewModel: IngestionTimelineViewModel = hiltViewModel()
    IngestionTimelineContent(
        ingestion = ingestion,
        roaDuration = viewModel.roaDuration,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun IngestionTimelineContentPreview(
    @PreviewParameter(
        IngestionTimelineProvider::class,
        limit = 1
    ) ingestionAndDuration: Pair<Ingestion, RoaDuration>,
) {
    IngestionTimelineContent(
        ingestion = ingestionAndDuration.first,
        roaDuration = ingestionAndDuration.second,
        modifier = Modifier
            .width(400.dp)
            .height(200.dp)
    )
}

@Composable
fun IngestionTimelineContent(
    ingestion: Ingestion,
    roaDuration: RoaDuration?,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()
    val color = ingestion.color.getComposeColor(isDarkTheme)
    if (roaDuration != null) {
        RoaDurationTimeline(roaDuration = roaDuration, color = color, modifier = modifier)
    } else {
        Text(text = "No roa duration")
    }
}

