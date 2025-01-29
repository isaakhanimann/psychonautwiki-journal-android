/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import com.isaakhanimann.journal.data.room.experiences.entities.AdaptiveColor
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.WeightedLine
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawTimeRange
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines.*
import java.time.Duration
import java.time.Instant
import kotlin.math.max

class GroupDrawable(
    val startTimeGraph: Instant,
    val color: AdaptiveColor,
    roaDuration: RoaDuration?,
    weightedLines: List<WeightedLine>,
    val areSubstanceHeightsIndependent: Boolean,
) : TimelineDrawable {
    private val timelineDrawables: List<TimelineDrawable>
    private val timeRangeDrawables: List<TimeRangeDrawable>
    private val nonNormalisedMaxOfRoute: Float

    override var referenceHeight: Float = 1f
    override val nonNormalisedHeight: Float

    init {
        val intermediateRanges = weightedLines.mapNotNull {
            if (it.endTime != null) {
                val startInSeconds =
                    Duration.between(startTimeGraph, it.startTime).seconds.toFloat()
                val endInSeconds = Duration.between(startTimeGraph, it.endTime).seconds.toFloat()
                return@mapNotNull TimeRangeDrawable.IntermediateRepresentation(
                    startInSeconds = startInSeconds,
                    endInSeconds = endInSeconds,
                    fullTimelineDurations = roaDuration?.toFullTimelineDurations(),
                    height = it.height
                )
            } else {
                return@mapNotNull null
            }
        }.sortedBy { it.startInSeconds }
        val timeRangeDrawables = intermediateRanges.mapIndexed { index, currentRange ->
            val intersectionCount = intermediateRanges.subList(0, index).count {
                it.startInSeconds <= currentRange.endInSeconds && it.endInSeconds >= currentRange.startInSeconds
            }
            val ingestionStartInSeconds = currentRange.startInSeconds
            val ingestionEndInSeconds = currentRange.endInSeconds
            TimeRangeDrawable(
                color = color,
                ingestionStartInSeconds = ingestionStartInSeconds,
                ingestionEndInSeconds = ingestionEndInSeconds,
                intersectionCountWithPreviousRanges = intersectionCount,
            )
        }
        val weightedLinesForPointIngestions = weightedLines.filter { it.endTime == null }
        this.timeRangeDrawables = timeRangeDrawables

        timelineDrawables = if (weightedLines.isNotEmpty()) {
            val fulls = roaDuration?.toFullTimelines(
                weightedLines = weightedLines,
                startTimeGraph = startTimeGraph,
            )
            if (fulls != null) {
                listOf(fulls)
            } else if (weightedLinesForPointIngestions.isNotEmpty()) {
                val onsetComeupPeakTotals = weightedLinesForPointIngestions.mapNotNull {
                    roaDuration?.toOnsetComeupPeakTotalTimeline(
                        peakAndTotalWeight = it.horizontalWeight,
                        ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                            it.startTime
                        ),
                        nonNormalisedHeight = it.height,
                    )
                }
                onsetComeupPeakTotals.ifEmpty {
                    val onsetComeupTotals = weightedLinesForPointIngestions.mapNotNull {
                        roaDuration?.toOnsetComeupTotalTimeline(
                            totalWeight = it.horizontalWeight,
                            ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                it.startTime
                            ),
                            nonNormalisedHeight = it.height,
                        )
                    }
                    onsetComeupTotals.ifEmpty {
                        val onsetTotals = weightedLinesForPointIngestions.mapNotNull {
                            roaDuration?.toOnsetTotalTimeline(
                                totalWeight = it.horizontalWeight,
                                ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                    it.startTime
                                ),
                                nonNormalisedHeight = it.height,
                            )
                        }
                        onsetTotals.ifEmpty {
                            val totals = weightedLinesForPointIngestions.mapNotNull {
                                roaDuration?.toTotalTimeline(
                                    totalWeight = it.horizontalWeight,
                                    ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                        it.startTime
                                    ),
                                    nonNormalisedHeight = it.height,
                                )
                            }
                            totals.ifEmpty {
                                val onsetComeupPeaks = weightedLinesForPointIngestions.mapNotNull {
                                    roaDuration?.toOnsetComeupPeakTimeline(
                                        peakWeight = it.horizontalWeight,
                                        ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                            it.startTime
                                        ),
                                        nonNormalisedHeight = it.height,
                                    )
                                }
                                onsetComeupPeaks.ifEmpty {
                                    val onsetComeups = weightedLinesForPointIngestions.mapNotNull {
                                        roaDuration?.toOnsetComeupTimeline(
                                            ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                                it.startTime
                                            ),
                                            nonNormalisedHeight = it.height,
                                        )
                                    }
                                    onsetComeups.ifEmpty {
                                        val onsets = weightedLinesForPointIngestions.mapNotNull {
                                            roaDuration?.toOnsetTimeline(
                                                ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                                    it.startTime
                                                )
                                            )
                                        }
                                        onsets.ifEmpty {
                                            weightedLinesForPointIngestions.map {
                                                NoTimeline(
                                                    ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                                        it.startTime
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
        val pointHeights = timelineDrawables.map { it.nonNormalisedHeight }
        val nonNormalisedMaxOfRoute = pointHeights.maxOrNull() ?: 1f
        this.nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute

        val finalPointHeights = timelineDrawables.map { it.nonNormalisedHeight }
        nonNormalisedHeight = finalPointHeights.maxOrNull() ?: 1f
    }

    fun normaliseHeight(overallMaxHeight: Float) {
        this.referenceHeight = overallMaxHeight

        val finalNonNormalisedMaxHeight: Float = if (areSubstanceHeightsIndependent) {
            nonNormalisedMaxOfRoute
        } else {
            overallMaxHeight
        }
        timelineDrawables.forEach { it.referenceHeight = finalNonNormalisedMaxHeight }
    }

    private fun getDistanceFromStartGraphInSeconds(time: Instant): Float {
        return Duration.between(startTimeGraph, time).seconds.toFloat()
    }

    override fun drawTimeLine(
        drawScope: DrawScope,
        canvasHeight: Float,
        pixelsPerSec: Float,
        color: Color,
        density: Density
    ) {
        for (drawable in timelineDrawables) {
            drawable.drawTimeLine(
                drawScope = drawScope,
                canvasHeight = canvasHeight,
                pixelsPerSec = pixelsPerSec,
                color = color,
                density = density
            )
        }
        for (rangeDrawable in timeRangeDrawables) {
            drawScope.drawTimeRange(
                timeRangeDrawable = rangeDrawable,
                canvasHeight = canvasHeight,
                pixelsPerSec = pixelsPerSec,
                color = color,
            )
        }
    }

    override val endOfLineRelativeToStartInSeconds: Float
        get() {
            val maxWidthOfTimeRangeIngestions = timeRangeDrawables.maxOfOrNull { it.ingestionEndInSeconds } ?: 0f
            val maxWidthOfPointIngestions =
                timelineDrawables.maxOfOrNull { it.endOfLineRelativeToStartInSeconds } ?: 0f
            return max(maxWidthOfTimeRangeIngestions, maxWidthOfPointIngestions)
        }
}