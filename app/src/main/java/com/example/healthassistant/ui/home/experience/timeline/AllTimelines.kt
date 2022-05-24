package com.example.healthassistant.ui.home.experience.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.example.healthassistant.data.experiences.entities.Ingestion
import com.example.healthassistant.data.experiences.entities.IngestionColor
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.home.experience.timeline.ingestion.TimelineDrawable
import com.example.healthassistant.ui.home.experience.timeline.ingestion.toFullTimeline
import com.example.healthassistant.ui.home.experience.timeline.ingestion.toTotalTimeline
import com.example.healthassistant.ui.previewproviders.TimelinesPreviewProvider
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration


data class AllTimelinesModel(
    val startTime: Date,
    val width: Duration,
    val ingestionDrawables: List<IngestionDrawable>
)

data class IngestionDrawable(
    val color: IngestionColor,
    val ingestionPointDistanceFromStart: Duration,
    val timelineDrawable: TimelineDrawable?
)

@Preview
@Composable
fun AllTimelines(
    @PreviewParameter(
        TimelinesPreviewProvider::class,
        limit = 1
    ) ingestionDurationPairs: List<Pair<Ingestion, RoaDuration>>,
    strokeWidth: Float = 5f,
) {
    val model: AllTimelinesModel? = remember(ingestionDurationPairs) {
        if (ingestionDurationPairs.isEmpty()) {
            null
        }
        val startTime = ingestionDurationPairs.map { it.first.time }
            .reduce { acc, date -> if (acc.before(date)) acc else date }
        val ingestionDrawables: List<IngestionDrawable> = ingestionDurationPairs.map {
            val full = it.second.toFullTimeline()
            val total = it.second.toTotalTimeline()
            val timelineDrawable = full ?: total
            val ingestionPointDistanceFromStart: Duration =
                (it.first.time.time - startTime.time).toDuration(DurationUnit.MILLISECONDS)
            IngestionDrawable(
                color = it.first.color,
                ingestionPointDistanceFromStart = ingestionPointDistanceFromStart,
                timelineDrawable = timelineDrawable
            )
        }
        val width = ingestionDrawables.map {
            if (it.timelineDrawable != null) {
                it.timelineDrawable.width + it.ingestionPointDistanceFromStart
            } else {
                it.ingestionPointDistanceFromStart
            }
        }.maxOrNull()
        AllTimelinesModel(
            startTime = startTime,
            width = width ?: 5.0.hours,
            ingestionDrawables = ingestionDrawables
        )
    }
    if (model != null) {
        val isDarkTheme = isSystemInDarkTheme()
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasHeightOuter = size.height
            val canvasWidth = size.width
            val pixelsPerSec = canvasWidth / model.width.inWholeSeconds
            model.ingestionDrawables.forEach { ingestionDrawable ->
                val color = ingestionDrawable.color.getComposeColor(isDarkTheme)
                val startX = ingestionDrawable.ingestionPointDistanceFromStart.inWholeSeconds * pixelsPerSec
                ingestionDrawable.timelineDrawable?.let { timelineDrawable ->
                    inset(vertical = strokeWidth / 2) {
                        val canvasHeightInner = size.height
                        drawPath(
                            path = timelineDrawable.getStrokePath(
                                pixelsPerSec = pixelsPerSec,
                                height = canvasHeightInner,
                                startX = startX
                            ),
                            color = color,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round,
                                pathEffect = if (timelineDrawable.isDotted) PathEffect.dashPathEffect(
                                    floatArrayOf(10f, 15f)
                                ) else null
                            )
                        )
                    }
                    drawPath(
                        path = timelineDrawable.getFillPath(
                            pixelsPerSec = pixelsPerSec,
                            height = canvasHeightOuter,
                            startX = startX
                        ),
                        color = color.copy(alpha = 0.1f)
                    )
                }
            }
        }
    } else {
        Text(text = "There can be no timeline drawn")
    }
}