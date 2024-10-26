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
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.drawables.timelines.*
import java.time.Duration
import java.time.Instant

class GroupDrawable(
    val startTimeGraph: Instant,
    val color: AdaptiveColor,
    roaDuration: RoaDuration?,
    weightedLines: List<WeightedLine>,
    areSubstanceHeightsIndependent: Boolean,
) : TimelineDrawable {
    private val timelineDrawables: List<TimelineDrawable>

    override var nonNormalisedOverallHeight: Float = 1f
    override val nonNormalisedHeight: Float

    init {
        val nonNormalisedMaxOfRoute = weightedLines.maxOfOrNull { it.height } ?: 1f
        val fulls = roaDuration?.toFullTimelines(
            weightedLines = weightedLines,
            startTimeGraph = startTimeGraph,
            areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
        )
        timelineDrawables = if (fulls != null) {
            listOf(fulls)
        } else {
            val onsetComeupPeakTotals = weightedLines.mapNotNull {
                roaDuration?.toOnsetComeupPeakTotalTimeline(
                    peakAndTotalWeight = it.horizontalWeight,
                    ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(it.startTime),
                    nonNormalisedHeight = it.height,
                    areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
                    nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
                )
            }
            onsetComeupPeakTotals.ifEmpty {
                val onsetComeupTotals = weightedLines.mapNotNull {
                    roaDuration?.toOnsetComeupTotalTimeline(
                        totalWeight = it.horizontalWeight,
                        ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                            it.startTime
                        ),
                        nonNormalisedHeight = it.height,
                        areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
                        nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
                    )
                }
                onsetComeupTotals.ifEmpty {
                    val onsetTotals = weightedLines.mapNotNull {
                        roaDuration?.toOnsetTotalTimeline(
                            totalWeight = it.horizontalWeight,
                            ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                it.startTime
                            ),
                            nonNormalisedHeight = it.height,
                            areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
                            nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
                        )
                    }
                    onsetTotals.ifEmpty {
                        val totals = weightedLines.mapNotNull {
                            roaDuration?.toTotalTimeline(
                                totalWeight = it.horizontalWeight,
                                ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                    it.startTime
                                ),
                                nonNormalisedHeight = it.height,
                                areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
                                nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
                            )
                        }
                        totals.ifEmpty {
                            val onsetComeupPeaks = weightedLines.mapNotNull {
                                roaDuration?.toOnsetComeupPeakTimeline(
                                    peakWeight = it.horizontalWeight,
                                    ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                        it.startTime
                                    ),
                                    nonNormalisedHeight = it.height,
                                    areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
                                    nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
                                )
                            }
                            onsetComeupPeaks.ifEmpty {
                                val onsetComeups = weightedLines.mapNotNull {
                                    roaDuration?.toOnsetComeupTimeline(
                                        ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                            it.startTime
                                        ),
                                        nonNormalisedHeight = it.height,
                                        areSubstanceHeightsIndependent = areSubstanceHeightsIndependent,
                                        nonNormalisedMaxOfRoute = nonNormalisedMaxOfRoute
                                    )
                                }
                                onsetComeups.ifEmpty {
                                    val onsets = weightedLines.mapNotNull {
                                        roaDuration?.toOnsetTimeline(
                                            ingestionTimeRelativeToStartInSeconds = getDistanceFromStartGraphInSeconds(
                                                it.startTime
                                            )
                                        )
                                    }
                                    onsets.ifEmpty {
                                        weightedLines.map {
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
        }
        nonNormalisedHeight = timelineDrawables.maxOfOrNull { it.nonNormalisedHeight } ?: 0f
    }

    override fun setOverallHeight(overallHeight: Float) {
        nonNormalisedOverallHeight = overallHeight
        timelineDrawables.forEach { it.setOverallHeight(overallHeight) }
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
    }

    override val endOfLineRelativeToStartInSeconds: Float
        get() = timelineDrawables.maxOfOrNull { it.endOfLineRelativeToStartInSeconds } ?: 0f
}