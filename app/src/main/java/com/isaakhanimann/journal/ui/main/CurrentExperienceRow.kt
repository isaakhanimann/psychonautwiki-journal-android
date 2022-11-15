/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.journal.ui.journal.ExperienceCircle
import com.isaakhanimann.journal.ui.journal.ExperienceWithIngestionsPreviewProvider
import com.isaakhanimann.journal.ui.theme.horizontalPadding


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun CurrentExperienceRow(
    @PreviewParameter(ExperienceWithIngestionsPreviewProvider::class) experienceWithIngestionsAndCompanions: ExperienceWithIngestionsAndCompanions,
    navigateToExperienceScreen: () -> Unit = {},
) {
    Card(
        onClick = navigateToExperienceScreen,
        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .height(48.dp), // Cards have a minimum height of 48.dp
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val ingestions = experienceWithIngestionsAndCompanions.ingestionsWithCompanions
            val experience = experienceWithIngestionsAndCompanions.experience
            ExperienceCircle(ingestions = ingestions, circleSize = 25.dp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = experience.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(2f)
            )
            Spacer(modifier = Modifier.width(5.dp))
            val substanceNames = remember(ingestions) {
                ingestions.map { it.ingestion.substanceName }.distinct()
                    .joinToString(separator = ", ")
            }
            val substanceText =
                substanceNames.ifEmpty { "No substance yet" }
            Text(
                text = substanceText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }
    }
}