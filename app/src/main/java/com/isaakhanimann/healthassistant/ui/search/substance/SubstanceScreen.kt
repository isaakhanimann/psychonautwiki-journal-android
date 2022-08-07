package com.isaakhanimann.healthassistant.ui.search.substance

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import com.isaakhanimann.healthassistant.ui.search.substance.roa.RoaView
import com.isaakhanimann.healthassistant.ui.search.substance.roa.ToleranceSection
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme
import kotlin.text.Typography

@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    viewModel: SubstanceViewModel = hiltViewModel()
) {
    SubstanceScreen(
        navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
        navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
        navigateToSaferHallucinogensScreen = navigateToSaferHallucinogensScreen,
        navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
        navigateToSaferStimulantsScreen = navigateToSaferStimulantsScreen,
        navigateToVolumetricDosingScreen = navigateToVolumetricDosingScreen,
        substance = viewModel.substance,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SubstanceScreenPreview(
    @PreviewParameter(SubstancePreviewProvider::class) substance: Substance
) {
    HealthAssistantTheme {
        SubstanceScreen(
            navigateToDosageExplanationScreen = {},
            navigateToDurationExplanationScreen = {},
            navigateToSaferHallucinogensScreen = {},
            navigateToSaferSniffingScreen = {},
            navigateToSaferStimulantsScreen = {},
            navigateToVolumetricDosingScreen = {},
            substance = substance,
            isSearchingForInteractions = true,
            dangerousInteractions = substance.interactions?.dangerous ?: emptyList(),
            unsafeInteractions = substance.interactions?.unsafe ?: emptyList(),
            uncertainInteractions = substance.interactions?.uncertain ?: emptyList(),
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
    substance: Substance,
    isSearchingForInteractions: Boolean,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(substance.name) },
                actions = {
                    ArticleLink(url = substance.url)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val titleStyle = MaterialTheme.typography.subtitle2
            if (substance.summary != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Summary", style = titleStyle)
                Text(text = substance.summary)
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (substance.effectsSummary != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Effects", style = titleStyle)
                Text(text = substance.effectsSummary)
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (substance.generalRisks != null && substance.longtermRisks != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Risks", style = titleStyle)
                Text(text = substance.generalRisks)
                Text(text = "Long-term", style = titleStyle)
                Text(text = substance.longtermRisks)
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (substance.saferUse.isNotEmpty()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Safer Use", style = titleStyle)
                val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 15.sp))
                Text(
                    buildAnnotatedString {
                        substance.saferUse.forEach {
                            withStyle(style = paragraphStyle) {
                                append(Typography.bullet)
                                append("\t\t")
                                append(it)
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (substance.dosageRemark != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Dosage Remark", style = titleStyle)
                Text(text = substance.dosageRemark)
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
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
            substance.roas.forEach { roa ->
                RoaView(
                    navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
                    navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
                    navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
                    roa = roa,
                    maxDurationInSeconds = maxDuration
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            Spacer(modifier = Modifier.height(5.dp))
            InteractionsView(
                isSearchingForInteractions = isSearchingForInteractions,
                dangerousInteractions = dangerousInteractions,
                unsafeInteractions = unsafeInteractions,
                uncertainInteractions = uncertainInteractions
            )
            ToleranceSection(
                tolerance = substance.tolerance,
                crossTolerances = substance.crossTolerances,
                titleStyle
            )
            if (substance.toxicities.isNotEmpty()) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Toxicity", style = titleStyle)
                val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = 15.sp))
                Text(
                    buildAnnotatedString {
                        if (substance.toxicities.size == 1) {
                            append(substance.toxicities.first())
                        } else {
                            substance.toxicities.forEach {
                                withStyle(style = paragraphStyle) {
                                    append(Typography.bullet)
                                    append("\t\t")
                                    append(it)
                                }
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (substance.addictionPotential != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "Addiction Potential", style = titleStyle)
                Text(substance.addictionPotential)
                Spacer(modifier = Modifier.height(5.dp))
                Divider()
            }
            if (substance.isHallucinogen) {
                TextButton(onClick = navigateToSaferHallucinogensScreen) {
                    Text(text = "Safer Hallucinogen Use")
                }
                Divider()
            }
            if (substance.isStimulant) {
                TextButton(onClick = navigateToSaferStimulantsScreen) {
                    Text(text = "Safer Stimulant Use")
                }
                Divider()
            }
            val firstRoa = substance.roas.firstOrNull()
            if (firstRoa?.roaDose?.shouldDefinitelyUseVolumetricDosing == true) {
                TextButton(onClick = navigateToVolumetricDosingScreen) {
                    Text(text = "Volumetric Liquid Dosing")
                }
                Divider()
            }
        }
    }
}