package com.example.healthassistant.ui.search.substance

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Effect
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.search.substance.roa.RoaView
import com.example.healthassistant.ui.theme.HealthAssistantTheme
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
    viewModel: SubstanceViewModel = hiltViewModel()
) {
    SubstanceScreen(
        navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
        navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
        navigateToSaferHallucinogensScreen = navigateToSaferHallucinogensScreen,
        navigateToSaferSniffingScreen = navigateToSaferSniffingScreen,
        substance = viewModel.substance,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
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
            substance = substance,
            isSearchingForInteractions = true,
            dangerousInteractions = substance.dangerousInteractions,
            unsafeInteractions = substance.unsafeInteractions,
            uncertainInteractions = substance.uncertainInteractions,
        )
    }
}

@Composable
fun SubstanceScreen(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
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
            val titleStyle = MaterialTheme.typography.h6
            substance.roas.forEach { roa ->
                RoaView(
                    navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
                    navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
                    roa = roa,
                    maxDurationInSeconds = maxDuration
                )
                if (roa.route == AdministrationRoute.INSUFFLATED) {
                    TextButton(onClick = navigateToSaferSniffingScreen) {
                        Text(text = "Safer Sniffing")
                    }
                }
            }
            if (substance.roas.isNotEmpty()) {
                Divider()
            }
            InteractionsView(
                isSearchingForInteractions = isSearchingForInteractions,
                dangerousInteractions = dangerousInteractions,
                unsafeInteractions = unsafeInteractions,
                uncertainInteractions = uncertainInteractions
            )
            val tolerance = substance.tolerance
            if (tolerance != null) {
                Text(text = "Tolerance", style = titleStyle)
                Text(text = "zero: ${tolerance.zero}")
                Text(text = "half: ${tolerance.half}")
                Text(text = "full: ${tolerance.full}")
                Divider()
            }
            if (substance.crossTolerances.isNotEmpty()) {
                Text(text = "Cross Tolerance", style = titleStyle)
                substance.crossTolerances.forEach {
                    Text(text = it)
                }
                Divider()
            }
            if (substance.psychoactiveClasses.isNotEmpty()) {
                Text(text = "Psychoactive Class", style = titleStyle)
                substance.psychoactiveClasses.forEach {
                    Text(text = it)
                }
                Divider()
            }
            if (substance.chemicalClasses.isNotEmpty()) {
                Text(text = "Chemical Class", style = titleStyle)
                substance.chemicalClasses.forEach {
                    Text(text = it)
                }
                Divider()
            }
            if (substance.toxicity != null) {
                Text(text = "Toxicity", style = titleStyle)
                Text(text = substance.toxicity)
                Divider()
            }
            if (substance.addictionPotential != null) {
                Text(text = "Addiction Potential", style = titleStyle)
                Text(substance.addictionPotential)
                Divider()
            }
            if (substance.effects.isNotEmpty()) {
                Text(text = "Subjective Effects", style = titleStyle)
                FlowRow {
                    substance.effects.forEach {
                        EffectChip(effect = it)
                    }
                }
                Divider()
            }
            if (substance.isHallucinogen) {
                TextButton(onClick = navigateToSaferHallucinogensScreen) {
                    Text(text = "Safer Hallucinogen Use")
                }
                Divider()
            }
        }
    }
}

@Composable
fun EffectChip(effect: Effect) {
    val uriHandler = LocalUriHandler.current
    Surface(
        modifier = Modifier
            .padding(2.dp)
            .clickable {
                uriHandler.openUri(effect.url)
            },
        shape = RoundedCornerShape(20.dp),
        color = Color.Blue
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "link",
                tint = Color.White, modifier = Modifier.size(12.dp)
            )
            Text(
                effect.name,
                color = Color.White,
                style = MaterialTheme.typography.caption
            )
        }

    }
}