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

package com.isaakhanimann.journal.ui.tabs.journal.experience.timeline

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardWithTitle
import com.isaakhanimann.journal.ui.tabs.search.substance.BulletPoints
import com.isaakhanimann.journal.ui.tabs.search.substance.SectionText
import com.isaakhanimann.journal.ui.tabs.search.substance.VerticalSpace
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ExplainTimelineScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Timeline Info") },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            VerticalSpace()
            CardWithTitle(title = "Simplifying Assumptions") {
                val text = buildAnnotatedString {
                    append("To be able to draw the timeline with the given data it makes multiple simplifying assumptions that are often ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("false")
                    }
                    append(":")
                }
                SectionText(text = text)
                BulletPoints(
                    points = listOf(
                        "\"Taking e.g. x amount now will give the same effect as taking x amount later. There is no immediate tolerance.\"",
                        "\"Taking twice the dose will give twice the effect.\"",
                        "\"Duration ranges from PsychonautWiki can be applied for all kinds of dosages.\"",
                        "\"Oral ingestions are always on an empty stomach and therefore not delayed (by up to 4 hours).\""
                    )
                )
            }
            CardWithTitle(title = "Understanding the Timeline") {
                BulletPoints(
                    points = listOf(
                        "The timeline is drawn based on the onset, comeup, peak and offset (and sometimes total) from PsychonautWiki.",
                        "In the ideal case all 4 durations (onset, comeup, peak and offset) are defined and the line is drawn as averageOnset -> averageComeup -> weightedPeak -> weightedOffset, where \"weighted\" means that the given range has been linearly interpolated with the dose, so if a threshold dose was taken it takes the minimum of the range, if a heavy dose was taken it takes the max and for everything in between it linearly interpolates.",
                        "The timelines of substances which have onset, comeup, peak and offset defined are combined into a cumulative timeline by simply putting the individual lines on top of each other. This is not done for ingestions where either the onset, comeup, peak or offset duration is missing.",
                        "If any of the 4 durations are missing but the total duration is given, then the first defined durations are drawn and as soon as a missing duration is encountered it uses the total to infer the end of the timeline and draws a dotted line to the end. If the total is not given it just stops drawing the line. So if there is no timeline or part of the timeline is missing that means that the duration is not defined in PsychonautWiki. If you add the missing durations in PsychonautWiki, the full timeline will be shown in the next update.",
                        "The vertical trajectory of dotted lines is unknown. So if you see a dotted line that means it is not known how the effect develops over that timeframe. The only thing that is known is where the line will end.",
                    )
                )
            }
            CardWithTitle(title = "PsychonautWiki Durations") {
                SectionText(
                    text = "Duration refers to the length of time over which the subjective effects of a psychoactive substance manifest themselves.\n" +
                            "Duration can be broken down into 6 parts: (1) total duration (2) onset (3) come up (4) peak (5) offset and (6) after effects. Depending upon the substance consumed, each of these occurs in a separate and continuous fashion."
                )
                val titleStyle = MaterialTheme.typography.titleSmall
                Text(text = "Total", style = titleStyle)
                SectionText(text = "The total duration of a substance can be defined as the amount of time it takes for the effects of a substance to completely wear off into sobriety, starting from the moment the substance is first administered.")
                Text(text = "Onset", style = titleStyle)
                SectionText(text = "The onset phase can be defined as the period until the very first changes in perception (i.e. \"first alerts\") are able to be detected.")
                Text(text = "Come up", style = titleStyle)
                SectionText(text = "The \"come up\" phase can be defined as the period between the first noticeable changes in perception and the point of highest subjective intensity. This is colloquially known as \"coming up.\"")
                Text(text = "Peak", style = titleStyle)
                SectionText(text = "The peak phase can be defined as period of time in which the intensity of the substance's effects are at its height.")
                Text(text = "Offset", style = titleStyle)
                SectionText(text = "The offset phase can be defined as the amount of time in between the conclusion of the peak and shifting into a sober state. This is colloquially referred to as \"coming down.\"")
                Text(text = "After Effects", style = titleStyle)
                SectionText(
                    text = "The after effects can be defined as any residual effects which may remain after the experience has reached its conclusion. This is colloquially known as a \"hangover\" or an \"afterglow\" depending on the substance and usage.\n" +
                            "The after effects are not included as part of the total duration."
                )
            }
        }
    }
}

