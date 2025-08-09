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

package com.isaakhanimann.journal.ui.tabs.journal.experience

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.NoteAdd
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInFull
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExposurePlus2
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.R
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.ui.FULL_STOMACH_DISCLAIMER
import com.isaakhanimann.journal.ui.YOU
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.interactions.Interaction
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CardTitle
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.CumulativeDoseRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.IngestionTimeOrDurationText
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.InteractionRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.NoteOrRatingTimeOrDurationText
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.SavedTimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.TimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.getDurationText
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.ingestion.IngestionRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.rating.OverallRatingRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.rating.TimedRatingRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.timednote.TimedNoteRow
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.ConsumerWithIngestions
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.CumulativeDose
import com.isaakhanimann.journal.ui.tabs.journal.experience.models.OneExperienceScreenModel
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelines
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.utils.getDateWithWeekdayText
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun ExperienceScreen(
    viewModel: ExperienceViewModel = hiltViewModel(),
    navigateToAddIngestionSearch: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateToAddRatingScreen: () -> Unit,
    navigateToAddTimedNoteScreen: () -> Unit,
    navigateToEditRatingScreen: (ratingId: Int) -> Unit,
    navigateToEditTimedNoteScreen: (timedNoteId: Int) -> Unit,
    navigateToTimelineScreen: (consumerName: String) -> Unit,
    navigateBack: () -> Unit,
) {
    val ingestionsWithCompanions = viewModel.ingestionsWithCompanionsFlow.collectAsState().value
    val experience = viewModel.experienceFlow.collectAsState().value
    val isFavorite = viewModel.isFavoriteFlow.collectAsState().value
    val oneExperienceScreenModel = OneExperienceScreenModel(
        isFavorite = isFavorite,
        title = experience?.title ?: "",
        firstIngestionTime = ingestionsWithCompanions.minOfOrNull { it.ingestion.time }
            ?: experience?.sortDate ?: Instant.now(),
        notes = experience?.text ?: "",
        locationName = experience?.location?.name ?: "",
        isCurrentExperience = viewModel.isCurrentExperienceFlow.collectAsState().value,
        ingestionElements = viewModel.ingestionElementsFlow.collectAsState().value,
        cumulativeDoses = viewModel.cumulativeDosesFlow.collectAsState().value,
        interactions = viewModel.interactionsFlow.collectAsState().value,
        interactionExplanations = viewModel.interactionExplanationsFlow.collectAsState().value,
        ratings = viewModel.ratingsFlow.collectAsState().value,
        timedNotesSorted = viewModel.timedNotesSortedFlow.collectAsState().value,
        consumersWithIngestions = viewModel.consumersWithIngestionsFlow.collectAsState().value,
        dataForEffectLines = viewModel.dataForEffectTimelinesFlow.collectAsState().value
    )
    ExperienceScreen(
        oneExperienceScreenModel = oneExperienceScreenModel,
        timelineDisplayOption = viewModel.timelineDisplayOptionFlow.collectAsState().value,
        isOralDisclaimerHidden = viewModel.isOralTimelineDisclaimerHidden.collectAsState().value,
        onChangeIsOralDisclaimerHidden = viewModel::saveOralDisclaimerIsHidden,
        addIngestion = {
            viewModel.saveLastIngestionTimeOfExperience()
            navigateToAddIngestionSearch()
        },
        deleteExperience = viewModel::deleteExperience,
        navigateToEditExperienceScreen = navigateToEditExperienceScreen,
        navigateToExplainTimeline = navigateToExplainTimeline,
        navigateToIngestionScreen = navigateToIngestionScreen,
        navigateToAddRatingScreen = navigateToAddRatingScreen,
        navigateToAddTimedNoteScreen = navigateToAddTimedNoteScreen,
        navigateBack = navigateBack,
        saveIsFavorite = viewModel::saveIsFavorite,
        navigateToEditRatingScreen = navigateToEditRatingScreen,
        navigateToEditTimedNoteScreen = navigateToEditTimedNoteScreen,
        savedTimeDisplayOption = viewModel.savedTimeDisplayOption.collectAsState().value,
        timeDisplayOption = viewModel.timeDisplayOptionFlow.collectAsState().value,
        onChangeTimeDisplayOption = viewModel::saveTimeDisplayOption,
        navigateToTimelineScreen = navigateToTimelineScreen,
        areDosageDotsHidden = viewModel.areDosageDotsHiddenFlow.collectAsState().value,
        isTimelineHidden = viewModel.isTimelineHiddenFlow.collectAsState().value
    )
}

@Preview
@Composable
fun ExperienceScreenPreview(
    @PreviewParameter(
        OneExperienceScreenPreviewProvider::class,
        limit = 1
    ) oneExperienceScreenModel: OneExperienceScreenModel
) {
    JournalTheme {
        ExperienceScreen(
            oneExperienceScreenModel = oneExperienceScreenModel,
            timelineDisplayOption = TimelineDisplayOption.Loading,
            isOralDisclaimerHidden = false,
            onChangeIsOralDisclaimerHidden = {},
            addIngestion = {},
            deleteExperience = {},
            navigateToEditExperienceScreen = {},
            navigateToExplainTimeline = {},
            navigateToIngestionScreen = {},
            navigateToAddRatingScreen = {},
            navigateToAddTimedNoteScreen = {},
            navigateBack = {},
            saveIsFavorite = {},
            navigateToEditRatingScreen = {},
            navigateToEditTimedNoteScreen = {},
            savedTimeDisplayOption = SavedTimeDisplayOption.RELATIVE_TO_START,
            timeDisplayOption = TimeDisplayOption.RELATIVE_TO_START,
            onChangeTimeDisplayOption = {},
            navigateToTimelineScreen = {},
            areDosageDotsHidden = false,
            isTimelineHidden = false
        )
    }
}

@Composable
fun ExperienceScreen(
    oneExperienceScreenModel: OneExperienceScreenModel,
    timelineDisplayOption: TimelineDisplayOption,
    isOralDisclaimerHidden: Boolean,
    onChangeIsOralDisclaimerHidden: (Boolean) -> Unit,
    addIngestion: () -> Unit,
    deleteExperience: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    navigateToAddRatingScreen: () -> Unit,
    navigateToAddTimedNoteScreen: () -> Unit,
    navigateBack: () -> Unit,
    saveIsFavorite: (Boolean) -> Unit,
    navigateToEditRatingScreen: (ratingId: Int) -> Unit,
    navigateToEditTimedNoteScreen: (timedNoteId: Int) -> Unit,
    savedTimeDisplayOption: SavedTimeDisplayOption,
    timeDisplayOption: TimeDisplayOption,
    onChangeTimeDisplayOption: (SavedTimeDisplayOption) -> Unit,
    navigateToTimelineScreen: (consumerName: String) -> Unit,
    areDosageDotsHidden: Boolean,
    isTimelineHidden: Boolean,
) {
    Scaffold(
        topBar = {
            ExperienceTopBar(
                oneExperienceScreenModel = oneExperienceScreenModel,
                onChangeTimeDisplayOption = onChangeTimeDisplayOption,
                savedTimeDisplayOption = savedTimeDisplayOption,
                deleteExperience = deleteExperience,
                navigateBack = navigateBack,
                navigateToEditExperienceScreen = navigateToEditExperienceScreen,
                saveIsFavorite = saveIsFavorite,
                navigateToAddTimedNoteScreen = navigateToAddTimedNoteScreen,
                navigateToAddRatingScreen = navigateToAddRatingScreen,
                addIngestion = addIngestion
            )
        },
        floatingActionButton = {
            AddIngestionFAB(oneExperienceScreenModel, addIngestion)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            val verticalCardPadding = 4.dp
            MyTimelineSection(
                timelineDisplayOption = timelineDisplayOption,
                verticalCardPadding = verticalCardPadding,
                navigateToExplainTimeline = navigateToExplainTimeline,
                navigateToTimelineScreen = navigateToTimelineScreen,
                oneExperienceScreenModel = oneExperienceScreenModel,
                timeDisplayOption = timeDisplayOption,
                isOralDisclaimerHidden = isOralDisclaimerHidden,
                onChangeIsOralDisclaimerHidden = onChangeIsOralDisclaimerHidden,
            )
            if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
                MyIngestionList(
                    verticalCardPadding = verticalCardPadding,
                    oneExperienceScreenModel = oneExperienceScreenModel,
                    areDosageDotsHidden = areDosageDotsHidden,
                    navigateToIngestionScreen = navigateToIngestionScreen,
                    timeDisplayOption = timeDisplayOption
                )
            }
            val cumulativeDoses = oneExperienceScreenModel.cumulativeDoses
            if (cumulativeDoses.isNotEmpty()) {
                CumulativeDosesSection(
                    verticalCardPadding = verticalCardPadding,
                    cumulativeDoses = cumulativeDoses,
                    areDosageDotsHidden = areDosageDotsHidden
                )
            }
            val timedNotesSorted = oneExperienceScreenModel.timedNotesSorted
            if (timedNotesSorted.isNotEmpty()) {
                TimedNotesSection(
                    verticalCardPadding = verticalCardPadding,
                    timedNotesSorted = timedNotesSorted,
                    navigateToEditTimedNoteScreen = navigateToEditTimedNoteScreen,
                    timeDisplayOption = timeDisplayOption,
                    firstIngestionTime = oneExperienceScreenModel.firstIngestionTime
                )
            }
            if (oneExperienceScreenModel.ratings.isNotEmpty()) {
                ShulginRatingsSection(
                    verticalCardPadding = verticalCardPadding,
                    oneExperienceScreenModel = oneExperienceScreenModel,
                    navigateToEditRatingScreen = navigateToEditRatingScreen,
                    timeDisplayOption = timeDisplayOption
                )
            }
            val notes = oneExperienceScreenModel.notes
            if (notes.isNotBlank()) {
                NotesSection(
                    verticalCardPadding = verticalCardPadding,
                    navigateToEditExperienceScreen = navigateToEditExperienceScreen,
                    oneExperienceScreenModel = oneExperienceScreenModel
                )
            }
            oneExperienceScreenModel.consumersWithIngestions.forEach { consumerWithIngestions ->
                ConsumerSection(
                    verticalCardPadding = verticalCardPadding,
                    consumerWithIngestions = consumerWithIngestions,
                    navigateToTimelineScreen = navigateToTimelineScreen,
                    timeDisplayOption = timeDisplayOption,
                    areDosageDotsHidden = areDosageDotsHidden,
                    navigateToIngestionScreen = navigateToIngestionScreen,
                    isTimelineHidden = isTimelineHidden
                )
            }
            val interactions = oneExperienceScreenModel.interactions
            AnimatedVisibility(visible = interactions.isNotEmpty()) {
                ExperienceInteractionsSection(
                    verticalCardPadding = verticalCardPadding,
                    interactions = interactions,
                    oneExperienceScreenModel = oneExperienceScreenModel
                )
            }
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ExperienceInteractionsSection(
    verticalCardPadding: Dp,
    interactions: List<Interaction>,
    oneExperienceScreenModel: OneExperienceScreenModel
) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = verticalCardPadding)
    ) {
        CardTitle(title = stringResource(R.string.interactions))
        interactions.forEachIndexed { index, interaction ->
            InteractionRow(interaction = interaction)
            if (index < interactions.size - 1) {
                HorizontalDivider()
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = stringResource(R.string.explanations),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(horizontal = horizontalPadding)
        ) {
            oneExperienceScreenModel.interactionExplanations.forEach {
                val uriHandler = LocalUriHandler.current
                SuggestionChip(
                    onClick = {
                        uriHandler.openUri(it.url)
                    },
                    label = { Text(it.name) }
                )
            }
        }
    }
}

@Composable
private fun ConsumerSection(
    verticalCardPadding: Dp,
    consumerWithIngestions: ConsumerWithIngestions,
    navigateToTimelineScreen: (consumerName: String) -> Unit,
    timeDisplayOption: TimeDisplayOption,
    areDosageDotsHidden: Boolean,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    isTimelineHidden: Boolean
) {
    ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardTitle(title = consumerWithIngestions.consumerName)
            if (!isTimelineHidden) {
                IconButton(onClick = { navigateToTimelineScreen(consumerWithIngestions.consumerName) }) {
                    Icon(
                        Icons.Default.OpenInFull,
                        contentDescription = stringResource(R.string.expand_timeline)
                    )
                }
            }
        }
        when (val timelineDisplayOption = consumerWithIngestions.timelineDisplayOption) {
            TimelineDisplayOption.Hidden -> {}
            TimelineDisplayOption.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            TimelineDisplayOption.NotWorthDrawing -> {}
            is TimelineDisplayOption.Shown -> {
                val timelineModel = timelineDisplayOption.allTimelinesModel
                Column(
                    modifier = Modifier
                        .padding(horizontal = horizontalPadding)
                        .padding(bottom = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    AllTimelines(
                        model = timelineModel,
                        timeDisplayOption = timeDisplayOption,
                        isShowingCurrentTime = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }
        HorizontalDivider()
        consumerWithIngestions.ingestionElements.forEachIndexed { index, ingestionElement ->
            IngestionRow(
                ingestionElement = ingestionElement,
                areDosageDotsHidden = areDosageDotsHidden,
                modifier = Modifier
                    .clickable {
                        navigateToIngestionScreen(ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion.id)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = horizontalPadding)
            ) {
                val ingestion = ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion
                IngestionTimeOrDurationText(
                    time = ingestion.time,
                    endTime = ingestion.endTime,
                    index = index,
                    timeDisplayOption = timeDisplayOption,
                    allTimesSortedMap = consumerWithIngestions.ingestionElements.map { it.ingestionWithCompanionAndCustomUnit.ingestion.time }
                )
            }
            if (index < consumerWithIngestions.ingestionElements.size - 1) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun NotesSection(
    verticalCardPadding: Dp,
    navigateToEditExperienceScreen: () -> Unit,
    oneExperienceScreenModel: OneExperienceScreenModel
) {
    ElevatedCard(modifier = Modifier
        .padding(vertical = verticalCardPadding)
        .fillMaxWidth()
        .clickable { navigateToEditExperienceScreen() }) {
        CardTitle(title = stringResource(R.string.notes))
        Column(
            modifier = Modifier
                .padding(horizontal = horizontalPadding)
                .padding(bottom = 10.dp)
        ) {
            Text(text = oneExperienceScreenModel.notes)
            if (oneExperienceScreenModel.locationName.isNotBlank()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = stringResource(R.string.location, oneExperienceScreenModel.locationName))
            }
        }
    }
}

@Composable
private fun ShulginRatingsSection(
    verticalCardPadding: Dp,
    oneExperienceScreenModel: OneExperienceScreenModel,
    navigateToEditRatingScreen: (ratingId: Int) -> Unit,
    timeDisplayOption: TimeDisplayOption
) {
    ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
        CardTitle(title = stringResource(R.string.shulgin_ratings))
        HorizontalDivider()
        val ratingsWithTime =
            oneExperienceScreenModel.ratings.mapNotNull { rating ->
                val time = rating.time
                if (time != null) {
                    Pair(time, rating)
                } else {
                    null
                }
            }.sortedBy { it.first }
        ratingsWithTime.forEachIndexed { index, pair ->
            TimedRatingRow(
                modifier = Modifier
                    .clickable {
                        navigateToEditRatingScreen(pair.second.id)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = horizontalPadding),
                ratingSign = pair.second.option.sign) {
                NoteOrRatingTimeOrDurationText(
                    time = pair.first,
                    timeDisplayOption = timeDisplayOption,
                    firstIngestionTime = oneExperienceScreenModel.firstIngestionTime
                )
            }
            if (index < ratingsWithTime.size - 1) {
                HorizontalDivider()
            }
        }
        val overallRating =
            oneExperienceScreenModel.ratings.firstOrNull { it.time == null }
        if (overallRating != null) {
            if (ratingsWithTime.isNotEmpty()) {
                HorizontalDivider()
            }
            OverallRatingRow(
                modifier = Modifier
                    .clickable {
                        navigateToEditRatingScreen(overallRating.id)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = horizontalPadding),
                ratingSign = overallRating.option.sign
            )
        }
    }
}

@Composable
private fun TimedNotesSection(
    verticalCardPadding: Dp,
    timedNotesSorted: List<TimedNote>,
    navigateToEditTimedNoteScreen: (timedNoteId: Int) -> Unit,
    timeDisplayOption: TimeDisplayOption,
    firstIngestionTime: Instant,
) {
    ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
        CardTitle(title = stringResource(R.string.timed_notes))
        if (timedNotesSorted.isNotEmpty()) {
            HorizontalDivider()
        }
        timedNotesSorted.forEachIndexed { index, timedNote ->
            TimedNoteRow(
                timedNote = timedNote,
                modifier = Modifier
                    .clickable {
                        navigateToEditTimedNoteScreen(timedNote.id)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = horizontalPadding)
            ) {
                NoteOrRatingTimeOrDurationText(
                    time = timedNote.time,
                    timeDisplayOption = timeDisplayOption,
                    firstIngestionTime = firstIngestionTime
                )
            }
            if (index < timedNotesSorted.size - 1) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun CumulativeDosesSection(
    verticalCardPadding: Dp,
    cumulativeDoses: List<CumulativeDose>,
    areDosageDotsHidden: Boolean
) {
    ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
        CardTitle(title = stringResource(R.string.your_cumulative_doses))
        if (cumulativeDoses.isNotEmpty()) {
            HorizontalDivider()
        }
        cumulativeDoses.forEachIndexed { index, cumulativeDose ->
            CumulativeDoseRow(
                cumulativeDose = cumulativeDose,
                areDosageDotsHidden = areDosageDotsHidden,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = horizontalPadding)
            )
            if (index < cumulativeDoses.size - 1) {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun MyIngestionList(
    verticalCardPadding: Dp,
    oneExperienceScreenModel: OneExperienceScreenModel,
    areDosageDotsHidden: Boolean,
    navigateToIngestionScreen: (ingestionId: Int) -> Unit,
    timeDisplayOption: TimeDisplayOption
) {
    ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
        CardTitle(
            title = oneExperienceScreenModel.firstIngestionTime.getDateWithWeekdayText()
        )
        if (oneExperienceScreenModel.ingestionElements.isNotEmpty()) {
            HorizontalDivider()
        }
        oneExperienceScreenModel.ingestionElements.forEachIndexed { index, ingestionElement ->
            IngestionRow(
                ingestionElement = ingestionElement,
                areDosageDotsHidden = areDosageDotsHidden,
                modifier = Modifier
                    .clickable {
                        navigateToIngestionScreen(ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion.id)
                    }
                    .fillMaxWidth()
                    .padding(vertical = 5.dp, horizontal = horizontalPadding)
            ) {
                val ingestion = ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion
                IngestionTimeOrDurationText(
                    time = ingestion.time,
                    endTime = ingestion.endTime,
                    index = index,
                    timeDisplayOption = timeDisplayOption,
                    allTimesSortedMap = oneExperienceScreenModel.ingestionElements.map { it.ingestionWithCompanionAndCustomUnit.ingestion.time }
                )
            }
            val isLastIngestion =
                index == oneExperienceScreenModel.ingestionElements.size - 1
            if (isLastIngestion) {
                if (oneExperienceScreenModel.isCurrentExperience) {
                    if (timeDisplayOption == TimeDisplayOption.TIME_BETWEEN) {
                        HorizontalDivider()
                        LastIngestionRelativeToNowText(lastIngestionTime = ingestionElement.ingestionWithCompanionAndCustomUnit.ingestion.time)
                    } else if (timeDisplayOption == TimeDisplayOption.RELATIVE_TO_START) {
                        HorizontalDivider()
                        NowRelativeToStartTimeText(startTime = oneExperienceScreenModel.firstIngestionTime)
                    }
                }
            } else {
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun MyTimelineSection(
    timelineDisplayOption: TimelineDisplayOption,
    verticalCardPadding: Dp,
    navigateToExplainTimeline: () -> Unit,
    navigateToTimelineScreen: (consumerName: String) -> Unit,
    oneExperienceScreenModel: OneExperienceScreenModel,
    timeDisplayOption: TimeDisplayOption,
    isOralDisclaimerHidden: Boolean,
    onChangeIsOralDisclaimerHidden: (Boolean) -> Unit,
) = when (timelineDisplayOption) {
    TimelineDisplayOption.Hidden -> {}
    TimelineDisplayOption.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
    TimelineDisplayOption.NotWorthDrawing -> {}
    is TimelineDisplayOption.Shown -> {
        val timelineModel = timelineDisplayOption.allTimelinesModel
        ElevatedCard(modifier = Modifier.padding(vertical = verticalCardPadding)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                CardTitle(title = stringResource(R.string.effect_timeline))
                Row {
                    TextButton(onClick = navigateToExplainTimeline) {
                        Text(text = stringResource(R.string.info))
                    }
                    IconButton(onClick = { navigateToTimelineScreen(YOU) }) {
                        Icon(
                            Icons.Default.OpenInFull,
                            contentDescription = stringResource(R.string.expand_timeline)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = horizontalPadding)
                    .padding(bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                AllTimelines(
                    model = timelineModel,
                    timeDisplayOption = timeDisplayOption,
                    isShowingCurrentTime = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                val hasOralIngestion =
                    oneExperienceScreenModel.ingestionElements.any { it.ingestionWithCompanionAndCustomUnit.ingestion.administrationRoute == AdministrationRoute.ORAL }
                if (hasOralIngestion && !isOralDisclaimerHidden) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = FULL_STOMACH_DISCLAIMER,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onChangeIsOralDisclaimerHidden(true) }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.close_disclaimer)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun AddIngestionFAB(
    oneExperienceScreenModel: OneExperienceScreenModel,
    addIngestion: () -> Unit
) {
    val wasAnyIngestionCreatedInLast4Hours =
        oneExperienceScreenModel.ingestionElements.mapNotNull { it.ingestionWithCompanionAndCustomUnit.ingestion.creationDate }
            .any {
                it > Instant.now().minus(4, ChronoUnit.HOURS)
            }
    if (oneExperienceScreenModel.isCurrentExperience || wasAnyIngestionCreatedInLast4Hours) {
        ExtendedFloatingActionButton(
            onClick = addIngestion,
            icon = {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add)
                )
            },
            text = { Text(stringResource(R.string.ingestion)) }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ExperienceTopBar(
    oneExperienceScreenModel: OneExperienceScreenModel,
    onChangeTimeDisplayOption: (SavedTimeDisplayOption) -> Unit,
    savedTimeDisplayOption: SavedTimeDisplayOption,
    deleteExperience: () -> Unit,
    navigateBack: () -> Unit,
    navigateToEditExperienceScreen: () -> Unit,
    saveIsFavorite: (Boolean) -> Unit,
    navigateToAddTimedNoteScreen: () -> Unit,
    navigateToAddRatingScreen: () -> Unit,
    addIngestion: () -> Unit
) {
    TopAppBar(
        title = { Text(oneExperienceScreenModel.title) },
        actions = {
            var areTimeOptionsExpanded by remember { mutableStateOf(false) }
            IconButton(onClick = { areTimeOptionsExpanded = true }) {
                Icon(Icons.Outlined.Timer, contentDescription = stringResource(R.string.time_display_option))
            }
            DropdownMenu(
                expanded = areTimeOptionsExpanded,
                onDismissRequest = { areTimeOptionsExpanded = false }
            ) {
                SavedTimeDisplayOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.text) },
                        onClick = {
                            onChangeTimeDisplayOption(option)
                            areTimeOptionsExpanded = false
                        },
                        leadingIcon = {
                            if (option == savedTimeDisplayOption) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = stringResource(R.string.check),
                                    modifier = Modifier.size(ButtonDefaults.IconSize)
                                )
                            }
                        }
                    )
                }
            }
            var areEditOptionsExpanded by remember { mutableStateOf(false) }
            IconButton(onClick = { areEditOptionsExpanded = true }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_options),
                )
            }
            var isShowingDeleteDialog by remember { mutableStateOf(false) }
            AnimatedVisibility(visible = isShowingDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { isShowingDeleteDialog = false },
                    title = {
                        Text(text = stringResource(R.string.delete_experience))
                    },
                    text = {
                        Text(stringResource(R.string.this_will_also_delete_all_its_ingestions))
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isShowingDeleteDialog = false
                                deleteExperience()
                                navigateBack()
                            }
                        ) {
                            Text(stringResource(R.string.delete))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { isShowingDeleteDialog = false }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                )
            }
            DropdownMenu(
                expanded = areEditOptionsExpanded,
                onDismissRequest = { areEditOptionsExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.edit_title_notes_location)) },
                    onClick = {
                        navigateToEditExperienceScreen()
                        areEditOptionsExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = stringResource(R.string.edit_experience),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                )
                val isFavorite = oneExperienceScreenModel.isFavorite
                if (isFavorite) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.unmark_favorite)) },
                        onClick = {
                            saveIsFavorite(false)
                            areEditOptionsExpanded = false
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = stringResource(R.string.unmark_favorite),
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                    )
                } else {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.mark_favorite)) },
                        onClick = {
                            saveIsFavorite(true)
                            areEditOptionsExpanded = false
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.StarOutline,
                                contentDescription = "Mark favorite",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                        }
                    )
                }
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.delete_experience)) },
                    onClick = {
                        isShowingDeleteDialog = true
                        areEditOptionsExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = stringResource(R.string.delete_experience),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                )
            }

            var areAddOptionsExpanded by remember { mutableStateOf(false) }
            IconButton(onClick = { areAddOptionsExpanded = true }) {
                Icon(
                    Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.add_options),
                )
            }
            DropdownMenu(
                expanded = areAddOptionsExpanded,
                onDismissRequest = { areAddOptionsExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.add_ingestion)) },
                    onClick = {
                        addIngestion()
                        areAddOptionsExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Add Ingestion",
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.add_timed_note)) },
                    onClick = {
                        navigateToAddTimedNoteScreen()
                        areAddOptionsExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.AutoMirrored.Outlined.NoteAdd,
                            contentDescription = stringResource(R.string.add_timed_note),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.add_shulgin_rating)) },
                    onClick = {
                        navigateToAddRatingScreen()
                        areAddOptionsExpanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.ExposurePlus2,
                            contentDescription = stringResource(R.string.add_shulgin_rating),
                            modifier = Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                )
            }
        }
    )
}

@Composable
private fun LastIngestionRelativeToNowText(lastIngestionTime: Instant) {
    val now: MutableState<Instant> = remember { mutableStateOf(Instant.now()) }
    LaunchedEffect(key1 = "updateTime") {
        while (true) {
            delay(10000L) // update every 10 seconds
            now.value = Instant.now()
        }
    }
    val isInPast = lastIngestionTime < now.value
    val relativeTime = if (isInPast) {
        stringResource(R.string.last_ingestion_was) + getDurationText(
            fromInstant = lastIngestionTime,
            toInstant = now.value
        ) + " ago"
    } else {
        stringResource(R.string.last_ingestion_in) + getDurationText(
            fromInstant = lastIngestionTime,
            toInstant = now.value
        )
    }
    Text(
        text = relativeTime,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = horizontalPadding)
    )
}

@Composable
private fun NowRelativeToStartTimeText(startTime: Instant) {
    val now: MutableState<Instant> = remember { mutableStateOf(Instant.now()) }
    LaunchedEffect(key1 = "updateTime") {
        while (true) {
            delay(10000L) // update every 10 seconds
            now.value = Instant.now()
        }
    }
    val isStartInPast = startTime < now.value
    val relativeTime = if (isStartInPast) {
        stringResource(R.string.now) + getDurationText(
            fromInstant = startTime,
            toInstant = now.value
        ) + stringResource(R.string.in_since_start)
    } else {
        stringResource(R.string.start_is_in) + getDurationText(
            fromInstant = startTime,
            toInstant = now.value
        )
    }
    Text(
        text = relativeTime,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = horizontalPadding)
    )
}