/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.search.substance

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.isaakhanimann.journal.data.substances.AdministrationRoute
import com.isaakhanimann.journal.data.substances.classes.Category
import com.isaakhanimann.journal.data.substances.classes.SubstanceWithCategories
import com.isaakhanimann.journal.ui.tabs.journal.addingestion.time.TimePickerButton
import com.isaakhanimann.journal.ui.doseDisclaimer
import com.isaakhanimann.journal.ui.tabs.journal.experience.DataForOneEffectLine
import com.isaakhanimann.journal.ui.tabs.journal.experience.timeline.AllTimelines
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.ToleranceSection
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.journal.ui.tabs.search.substance.roa.duration.RoaDurationView
import com.isaakhanimann.journal.ui.theme.JournalTheme
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.theme.verticalPaddingCards
import com.isaakhanimann.journal.ui.utils.getInstant
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.LocalDateTime

@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToArticle: (url: String) -> Unit,
    navigateToCategoryScreen: (categoryName: String) -> Unit,
    viewModel: SubstanceViewModel = hiltViewModel()
) {
    SubstanceScreen(
        navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
        navigateToSaferHallucinogensScreen = navigateToSaferHallucinogensScreen,
        navigateToSaferStimulantsScreen = navigateToSaferStimulantsScreen,
        navigateToVolumetricDosingScreen = navigateToVolumetricDosingScreen,
        navigateToCategoryScreen = navigateToCategoryScreen,
        navigateToExplainTimeline = navigateToExplainTimeline,
        navigateToURL = navigateToArticle,
        substanceWithCategories = viewModel.substanceWithCategories
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SubstanceScreenPreview(
    @PreviewParameter(SubstanceWithCategoriesPreviewProvider::class) substanceWithCategories: SubstanceWithCategories
) {
    JournalTheme {
        SubstanceScreen(
            navigateToDosageExplanationScreen = {},
            navigateToSaferHallucinogensScreen = {},
            navigateToSaferStimulantsScreen = {},
            navigateToVolumetricDosingScreen = {},
            navigateToExplainTimeline = {},
            navigateToURL = {},
            navigateToCategoryScreen = {},
            substanceWithCategories = substanceWithCategories
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToExplainTimeline: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    navigateToCategoryScreen: (categoryName: String) -> Unit,
    substanceWithCategories: SubstanceWithCategories
) {
    val substance = substanceWithCategories.substance
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(substance.name) })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToURL(substance.url) },
                icon = {
                    Icon(
                        Icons.Outlined.Article,
                        contentDescription = "Open Link"
                    )
                },
                text = { Text("More Info") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            if (!substance.isApproved) {
                Card(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = verticalPaddingCards
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(
                            vertical = 5.dp,
                            horizontal = horizontalPadding
                        )
                    ) {
                        Icon(imageVector = Icons.Default.GppBad, contentDescription = "Verified")
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(text = "Info is not approved")
                    }
                }
            }
            if (substance.summary != null) {
                VerticalSpace()
                Card(
                    modifier = Modifier.padding(
                        horizontal = horizontalPadding,
                        vertical = verticalPaddingCards
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = horizontalPadding,
                                vertical = 10.dp
                            )
                            .fillMaxWidth()
                    ) {
                        Text(text = substance.summary)
                        val categories = substanceWithCategories.categories
                        VerticalSpace()
                        FlowRow(
                            mainAxisSpacing = 5.dp,
                            crossAxisSpacing = 5.dp,
                        ) {
                            categories.forEach { category ->
                                CategoryChipFromSubstanceScreen(category, navigateToCategoryScreen)
                            }
                        }
                    }
                }
            }
            val roasWithDosesDefined = substance.roas.filter { roa ->
                val roaDose = roa.roaDose
                val isEveryDoseNull =
                    roaDose?.threshold == null && roaDose?.light == null && roaDose?.common == null && roaDose?.strong == null && roaDose?.heavy == null
                return@filter !isEveryDoseNull
            }
            if (substance.dosageRemark != null || roasWithDosesDefined.isNotEmpty()) {
                CollapsibleSection(title = "Dosage") {
                    Column(Modifier.padding(horizontal = horizontalPadding)) {
                        if (substance.dosageRemark != null) {
                            Text(text = substance.dosageRemark)
                            VerticalSpace()
                            Divider()
                        }
                        roasWithDosesDefined.forEach { roa ->
                            Column(
                                modifier = Modifier.padding(vertical = 5.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RouteColorCircle(roa.route)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = roa.route.displayText,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                if (roa.roaDose == null) {
                                    Text(text = "No dosage info")
                                } else {
                                    RoaDoseView(roaDose = roa.roaDose)
                                }
                            }
                            Divider()
                        }
                        VerticalSpace()
                        Text(text = doseDisclaimer)
                        TextButton(onClick = navigateToDosageExplanationScreen) {
                            Icon(
                                Icons.Outlined.Info,
                                contentDescription = "Info",
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Dosage Classification")
                        }
                    }
                }
            }
            if (substance.tolerance != null || substance.crossTolerances.isNotEmpty()) {
                CollapsibleSection(title = "Tolerance") {
                    Column {
                        VerticalSpace()
                        ToleranceSection(
                            tolerance = substance.tolerance,
                            crossTolerances = substance.crossTolerances,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        VerticalSpace()
                    }
                }
            }
            if (substance.toxicities.isNotEmpty()) {
                CollapsibleSection(title = "Toxicity") {
                    Column {
                        VerticalSpace()
                        if (substance.toxicities.size == 1) {
                            Text(
                                substance.toxicities.firstOrNull() ?: "",
                                modifier = Modifier.padding(horizontal = horizontalPadding)
                            )
                        } else {
                            BulletPoints(
                                points = substance.toxicities,
                                modifier = Modifier.padding(horizontal = horizontalPadding)
                            )
                        }
                        VerticalSpace()
                    }
                }
            }
            val roasWithDurationsDefined = substance.roas.filter { roa ->
                val roaDuration = roa.roaDuration
                val isEveryDurationNull =
                    roaDuration?.onset == null && roaDuration?.comeup == null && roaDuration?.peak == null && roaDuration?.offset == null && roaDuration?.total == null
                return@filter !isEveryDurationNull
            }
            if (roasWithDurationsDefined.isNotEmpty()) {
                CollapsibleSection(title = "Duration") {
                    Column(Modifier.padding(horizontal = horizontalPadding)) {
                        var ingestionTime by remember { mutableStateOf(LocalDateTime.now()) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text("Start:")
                            Spacer(modifier = Modifier.width(5.dp))
                            TimePickerButton(
                                localDateTime = ingestionTime,
                                onChange = { ingestionTime = it },
                                timeString = ingestionTime.getStringOfPattern("HH:mm"),
                                hasOutline = false,
                            )
                        }
                        VerticalSpace()
                        val dataForEffectLines = remember(roasWithDurationsDefined, ingestionTime) {
                            roasWithDurationsDefined.map { roa ->
                                DataForOneEffectLine(
                                    roaDuration = roa.roaDuration,
                                    height = 1f,
                                    horizontalWeight = 0.5f,
                                    color = roa.route.color,
                                    startTime = ingestionTime.getInstant()
                                )
                            }
                        }
                        AllTimelines(
                            dataForEffectLines = dataForEffectLines,
                            isShowingCurrentTime = false,
                            navigateToExplainTimeline = navigateToExplainTimeline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider()
                        roasWithDurationsDefined.forEachIndexed { index, roa ->
                            Column(
                                modifier = Modifier.padding(
                                    vertical = 5.dp,
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    RouteColorCircle(roa.route)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = roa.route.displayText,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                val roaDuration = roa.roaDuration
                                if (roaDuration == null) {
                                    Text(text = "No duration info")
                                } else {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    RoaDurationView(roaDuration = roaDuration)
                                }
                            }
                            if (index < roasWithDurationsDefined.size - 1) {
                                Divider()
                            }
                        }
                        VerticalSpace()
                    }
                }
            }
            val interactions = substance.interactions
            if (interactions != null) {
                if (interactions.dangerous.isNotEmpty() || interactions.unsafe.isNotEmpty() || interactions.uncertain.isNotEmpty()) {
                    CollapsibleSection(title = "Interactions") {
                        InteractionsView(
                            interactions = substance.interactions,
                            substanceURL = substance.url,
                            navigateToURL = navigateToURL
                        )
                    }
                }
            }
            if (substance.effectsSummary != null) {
                CollapsibleSection(title = "Effects") {
                    Column {
                        Text(
                            text = substance.effectsSummary,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            if (substance.generalRisks != null && substance.longtermRisks != null) {
                CollapsibleSection(title = "Risks") {
                    Column {
                        Text(
                            text = substance.generalRisks,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                CollapsibleSection(title = "Long-term") {
                    Column {
                        Text(
                            text = substance.longtermRisks,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            if (substance.saferUse.isNotEmpty()) {
                CollapsibleSection(title = "Safer Use") {
                    Column {
                        BulletPoints(
                            points = substance.saferUse,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            if (substance.addictionPotential != null) {
                CollapsibleSection(title = "Addiction Potential") {
                    Column {
                        Text(
                            substance.addictionPotential,
                            modifier = Modifier.padding(horizontal = horizontalPadding)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
            val firstRoa = substance.roas.firstOrNull()
            val useVolumetric = firstRoa?.roaDose?.shouldDefinitelyUseVolumetricDosing == true
            if (substance.isHallucinogen || substance.isStimulant || useVolumetric) {
                CollapsibleSection(title = "See Also") {
                    Column {
                        if (substance.isHallucinogen) {
                            TextButton(onClick = navigateToSaferHallucinogensScreen) {
                                Text(
                                    text = "Safer Hallucinogen Use",
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            }
                            Divider()
                        }
                        if (substance.isStimulant) {
                            TextButton(onClick = navigateToSaferStimulantsScreen) {
                                Text(
                                    text = "Safer Stimulant Use",
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            }
                            Divider()
                        }
                        if (useVolumetric) {
                            TextButton(onClick = navigateToVolumetricDosingScreen) {
                                Text(
                                    text = "Volumetric Liquid Dosing",
                                    modifier = Modifier.padding(horizontal = horizontalPadding)
                                )
                            }
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RouteColorCircle(administrationRoute: AdministrationRoute) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        shape = CircleShape,
        color = administrationRoute.color.getComposeColor(isDarkTheme),
        modifier = Modifier
            .size(20.dp)
    ) {}
}

@Composable
fun BulletPoints(points: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        points.forEach {
            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(top = 7.dp)
                        .size(7.dp)
                ) {}
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = it)
            }
        }
    }
}

@Composable
fun VerticalSpace() {
    Spacer(modifier = Modifier.height(5.dp))
}


@Composable
fun CategoryChipFromSubstanceScreen(
    category: Category,
    navigateToCategoryScreen: (categoryName: String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = CircleShape)
            .clickable {
                navigateToCategoryScreen(category.name)
            }
            .background(color = category.color.copy(alpha = 0.2f))
            .padding(vertical = 4.dp, horizontal = 10.dp)

    ) {
        Text(text = category.name)
        Spacer(modifier = Modifier.width(3.dp))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Go to",
            modifier = Modifier.size(20.dp)
        )
    }
}