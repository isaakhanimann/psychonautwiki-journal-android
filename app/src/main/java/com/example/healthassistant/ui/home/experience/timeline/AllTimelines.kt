package com.example.healthassistant.ui.home.experience.timeline

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.TimelinesPreviewProvider

@Preview
@Composable
fun AllTimelines(
    @PreviewParameter(
        TimelinesPreviewProvider::class,
        limit = 1
    ) ingestionDurationPairs: List<Pair<Ingestion, RoaDuration>>
) {
    Text(text = "Hello")
}