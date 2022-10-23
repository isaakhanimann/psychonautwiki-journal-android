package com.isaakhanimann.healthassistant.ui.search.substance

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.GppBad
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.Category
import com.isaakhanimann.healthassistant.data.substances.classes.SubstanceWithCategories
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.roa.RoaView
import com.isaakhanimann.healthassistant.ui.search.substance.roa.ToleranceSection
import com.isaakhanimann.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToCategoryScreen: (categoryName: String) -> Unit,
    viewModel: SubstanceViewModel = hiltViewModel()
) {
    SubstanceScreen(
        navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
        navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
        navigateToSaferHallucinogensScreen = navigateToSaferHallucinogensScreen,
        navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
        navigateToSaferStimulantsScreen = navigateToSaferStimulantsScreen,
        navigateToVolumetricDosingScreen = navigateToVolumetricDosingScreen,
        navigateToCategoryScreen = navigateToCategoryScreen,
        substanceWithCategories = viewModel.substanceWithCategories
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SubstanceScreenPreview(
    @PreviewParameter(SubstanceWithCategoriesPreviewProvider::class) substanceWithCategories: SubstanceWithCategories
) {
    HealthAssistantTheme {
        SubstanceScreen(
            navigateToDosageExplanationScreen = {},
            navigateToDurationExplanationScreen = {},
            navigateToSaferHallucinogensScreen = {},
            navigateToSaferSniffingScreen = {},
            navigateToSaferStimulantsScreen = {},
            navigateToVolumetricDosingScreen = {},
            navigateToCategoryScreen = {},
            substanceWithCategories = substanceWithCategories
        )
    }
}

@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToCategoryScreen: (categoryName: String) -> Unit,
    substanceWithCategories: SubstanceWithCategories
) {
    val substance = substanceWithCategories.substance
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            JournalTopAppBar(title = substance.name)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { uriHandler.openUri(substance.url) },
                icon = {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link"
                    )
                },
                text = { Text("More Info") },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            if (!substance.isApproved) {
                Row(modifier = Modifier.padding(vertical = 5.dp, horizontal = horizontalPadding)) {
                    Icon(imageVector = Icons.Default.GppBad, contentDescription = "Verified")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text = "Info is not approved")
                }
            }
            if (substance.summary != null) {
                VerticalSpace()
                Text(
                    text = substance.summary,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )
                val categories = substanceWithCategories.categories
                VerticalSpace()
                FlowRow(
                    mainAxisSpacing = 5.dp,
                    crossAxisSpacing = 5.dp,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    categories.forEach { category ->
                        CategoryChipFromSubstanceScreen(category, navigateToCategoryScreen)
                    }
                }
                VerticalSpace()
            }
            val maxDuration = remember(substance.roas) {
                substance.roas.mapNotNull {
                    val duration = it.roaDuration ?: return@mapNotNull null
                    val maxTotal = duration.total?.maxInSec
                    val maxOnset = duration.onset?.maxInSec
                    val maxComeup = duration.comeup?.maxInSec
                    val maxPeak = duration.peak?.maxInSec
                    val maxOffset = duration.offset?.maxInSec
                    val maxTimeline =
                        if (maxOnset != null && maxComeup != null && maxPeak != null && maxOffset != null) {
                            maxOnset + maxComeup + maxPeak + maxOffset
                        } else {
                            val partialSum = ((maxOnset ?: 0f) + (maxComeup ?: 0f) + (maxPeak
                                ?: 0f) + (maxOffset ?: 0f)).times(1.1f)
                            if (partialSum == 0f) {
                                null
                            } else {
                                partialSum
                            }
                        }
                    if (maxTotal == null && maxTimeline == null) {
                        return@mapNotNull null
                    } else if (maxTotal != null && maxTimeline != null) {
                        if (maxTotal > maxTimeline) {
                            return@mapNotNull maxTotal
                        } else {
                            return@mapNotNull maxTimeline
                        }
                    } else {
                        return@mapNotNull maxTotal ?: maxTimeline
                    }
                }.maxOrNull()
            }
            val roasWithDosesDefined = substance.roas.filter { roa ->
                val roaDose = roa.roaDose
                val isEveryDoseNull =
                    roaDose?.threshold == null && roaDose?.light == null && roaDose?.common == null && roaDose?.strong == null && roaDose?.heavy == null
                return@filter !isEveryDoseNull
            }
            if (substance.dosageRemark != null || roasWithDosesDefined.isNotEmpty()) {
                SectionTitle(title = "Dosage", onInfoClick = navigateToDosageExplanationScreen)
                if (substance.dosageRemark != null) {
                    VerticalSpace()
                    Text(text = substance.dosageRemark, modifier = Modifier.padding(horizontal = horizontalPadding))
                    VerticalSpace()
                    Divider()
                }
                roasWithDosesDefined.forEach { roa ->
                    Column(modifier = Modifier.padding(vertical = 5.dp, horizontal = horizontalPadding)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RouteColorCircle(roa.route)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = roa.route.displayText)
                        }
                        if (roa.roaDose == null) {
                            Text(text = "No dosage info")
                        } else {
                            RoaDoseView(roaDose = roa.roaDose)
                        }
                    }
                    Divider()
                }
            }
            SectionTitle(title = "Duration")
            VerticalSpace()
            substance.roas.forEach { roa ->
                RoaView(
                    navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
                    navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
                    navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
                    roa = roa,
                    maxDurationInSeconds = maxDuration
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            Spacer(modifier = Modifier.height(5.dp))
            val interactions = substance.interactions
            if (interactions != null) {
                if (interactions.dangerous.isNotEmpty() || interactions.unsafe.isNotEmpty() || interactions.uncertain.isNotEmpty()) {
                    SectionTitle(title = "Interactions")
                    InteractionsView(interactions = substance.interactions)
                }
            }
            if (substance.tolerance != null || substance.crossTolerances.isNotEmpty()) {
                SectionTitle(title = "Tolerance")
                VerticalSpace()
                ToleranceSection(
                    tolerance = substance.tolerance,
                    crossTolerances = substance.crossTolerances,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )
                VerticalSpace()
            }
            if (substance.toxicities.isNotEmpty()) {
                SectionTitle(title = "Toxicity")
                VerticalSpace()
                if (substance.toxicities.size == 1) {
                    Text(substance.toxicities.firstOrNull() ?: "", modifier = Modifier.padding(horizontal = horizontalPadding))
                } else {
                    BulletPoints(points = substance.toxicities, modifier = Modifier.padding(horizontal = horizontalPadding))
                }
                VerticalSpace()
            }
            if (substance.effectsSummary != null) {
                SectionTitle(title = "Effects")
                VerticalSpace()
                Text(text = substance.effectsSummary, modifier = Modifier.padding(horizontal = horizontalPadding))
                VerticalSpace()
            }
            if (substance.generalRisks != null && substance.longtermRisks != null) {
                SectionTitle(title = "Risks")
                VerticalSpace()
                Text(text = substance.generalRisks, modifier = Modifier.padding(horizontal = horizontalPadding))
                VerticalSpace()
                SectionTitle(title = "Long-term")
                VerticalSpace()
                Text(text = substance.longtermRisks, modifier = Modifier.padding(horizontal = horizontalPadding))
                VerticalSpace()
            }
            if (substance.saferUse.isNotEmpty()) {
                SectionTitle(title = "Safer Use")
                VerticalSpace()
                BulletPoints(points = substance.saferUse, modifier = Modifier.padding(horizontal = horizontalPadding))
                VerticalSpace()
            }
            if (substance.addictionPotential != null) {
                SectionTitle(title = "Addiction Potential")
                VerticalSpace()
                Text(substance.addictionPotential, modifier = Modifier.padding(horizontal = horizontalPadding))
                VerticalSpace()
            }
            val firstRoa = substance.roas.firstOrNull()
            val useVolumetric = firstRoa?.roaDose?.shouldDefinitelyUseVolumetricDosing == true
            if (substance.isHallucinogen || substance.isStimulant || useVolumetric) {
                SectionTitle(title = "See Also")
            }
            if (substance.isHallucinogen) {
                TextButton(onClick = navigateToSaferHallucinogensScreen) {
                    Text(text = "Safer Hallucinogen Use", modifier = Modifier.padding(horizontal = horizontalPadding))
                }
                Divider()
            }
            if (substance.isStimulant) {
                TextButton(onClick = navigateToSaferStimulantsScreen) {
                    Text(text = "Safer Stimulant Use", modifier = Modifier.padding(horizontal = horizontalPadding))
                }
                Divider()
            }
            if (useVolumetric) {
                TextButton(onClick = navigateToVolumetricDosingScreen) {
                    Text(text = "Volumetric Liquid Dosing", modifier = Modifier.padding(horizontal = horizontalPadding))
                }
                Divider()
            }
        }
    }
}

@Composable
fun RouteColorCircle(administrationRoute: AdministrationRoute) {
    val isDarkTheme = isSystemInDarkTheme()
    Surface(
        shape = CircleShape,
        color = administrationRoute.getComposeColor(isDarkTheme),
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
                    color = MaterialTheme.colors.onBackground,
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