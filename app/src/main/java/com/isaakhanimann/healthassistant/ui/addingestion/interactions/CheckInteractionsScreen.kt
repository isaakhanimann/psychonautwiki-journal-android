package com.isaakhanimann.healthassistant.ui.addingestion.interactions

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.data.substances.classes.InteractionType
import com.isaakhanimann.healthassistant.data.substances.classes.Substance
import com.isaakhanimann.healthassistant.ui.search.substance.InteractionChip
import com.isaakhanimann.healthassistant.ui.search.substance.SubstanceWithCategoriesPreviewProvider


@Composable
fun CheckInteractionsScreen(
    navigateToChooseRouteScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    CheckInteractionsScreen(
        substanceName = viewModel.substanceName,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        isShowingHallucinogenLink = viewModel.isShowingHallucinogenLink,
        isShowingStimulantsLink = viewModel.isShowingStimulantsLink,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
        navigateToNext = navigateToChooseRouteScreen,
        navigateToSaferHallucinogensScreen = navigateToSaferHallucinogensScreen,
        navigateToSaferStimulantsScreen = navigateToSaferStimulantsScreen,
        dismissAlert = {
            viewModel.isShowingAlert = false
        },
        isShowingAlert = viewModel.isShowingAlert,
        alertInteractionType = viewModel.alertInteractionType,
        alertText = viewModel.alertText
    )
}

@Preview
@Composable
fun CheckInteractionsScreenPreview(@PreviewParameter(SubstanceWithCategoriesPreviewProvider::class) substance: Substance) {
    CheckInteractionsScreen(
        substanceName = "LSD",
        isSearchingForInteractions = true,
        isShowingHallucinogenLink = true,
        isShowingStimulantsLink = false,
        dangerousInteractions = substance.interactions?.dangerous ?: emptyList(),
        unsafeInteractions = substance.interactions?.unsafe ?: emptyList(),
        uncertainInteractions = substance.interactions?.uncertain ?: emptyList(),
        navigateToNext = {},
        navigateToSaferHallucinogensScreen = {},
        navigateToSaferStimulantsScreen = {},
        dismissAlert = {},
        isShowingAlert = false,
        alertInteractionType = InteractionType.DANGEROUS,
        alertText = "Dangerous Interaction with Heroin taken 4h ago"
    )
}

@Preview
@Composable
fun CheckInteractionsScreenPreview2() {
    CheckInteractionsScreen(
        substanceName = "MDMA",
        isSearchingForInteractions = true,
        isShowingHallucinogenLink = false,
        isShowingStimulantsLink = false,
        dangerousInteractions = emptyList(),
        unsafeInteractions = emptyList(),
        uncertainInteractions = emptyList(),
        navigateToNext = {},
        navigateToSaferHallucinogensScreen = {},
        navigateToSaferStimulantsScreen = {},
        dismissAlert = {},
        isShowingAlert = true,
        alertInteractionType = InteractionType.DANGEROUS,
        alertText = "Dangerous Interaction with Heroin taken 4h ago."
    )
}

@Composable
fun CheckInteractionsScreen(
    substanceName: String,
    isShowingHallucinogenLink: Boolean,
    isShowingStimulantsLink: Boolean,
    isSearchingForInteractions: Boolean,
    isShowingAlert: Boolean,
    dismissAlert: () -> Unit,
    alertInteractionType: InteractionType?,
    alertText: String,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
    navigateToNext: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToSaferStimulantsScreen: () -> Unit,
) {

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Check Interactions With $substanceName") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToNext,
                icon = {
                    Icon(
                        Icons.Filled.NavigateNext,
                        contentDescription = "Next"
                    )
                },
                text = { Text("Next") },
            )
        }
    ) {
        Column {
            LinearProgressIndicator(progress = 0.33f, modifier = Modifier.fillMaxWidth())
            if (isSearchingForInteractions) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            if (isShowingHallucinogenLink) {
                TextButton(onClick = navigateToSaferHallucinogensScreen) {
                    Text(text = "Safer Hallucinogen Use")
                }
                Divider()
            }
            if (isShowingStimulantsLink) {
                TextButton(onClick = navigateToSaferStimulantsScreen) {
                    Text(text = "Safer Stimulant Use")
                }
                Divider()
            }
            if (dangerousInteractions.isEmpty() && unsafeInteractions.isEmpty() && uncertainInteractions.isEmpty()) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No interactions found... check other sources.")
                    }
                }
            } else {
                Column {
                    if (dangerousInteractions.isNotEmpty()) {
                        dangerousInteractions.forEach {
                            InteractionChip(text = it, color = InteractionType.DANGEROUS.color)
                        }
                    }
                    if (unsafeInteractions.isNotEmpty()) {
                        unsafeInteractions.forEach {
                            InteractionChip(text = it, color = InteractionType.UNSAFE.color)

                        }
                    }
                    if (uncertainInteractions.isNotEmpty()) {
                        uncertainInteractions.forEach {
                            InteractionChip(text = it, color = InteractionType.UNCERTAIN.color)
                        }
                    }
                    Text(
                        text = "Check the PsychonautWiki article for explanations",
                        style = MaterialTheme.typography.caption,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }
            }
            if (isShowingAlert && alertInteractionType != null) {
                AlertDialog(
                    onDismissRequest = dismissAlert,
                    title = {
                        val title = when (alertInteractionType) {
                            InteractionType.DANGEROUS -> "Dangerous Interaction!"
                            InteractionType.UNSAFE -> "Unsafe Interaction!"
                            InteractionType.UNCERTAIN -> "Uncertain Interaction!"
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = alertInteractionType.color
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = title, style = MaterialTheme.typography.h6)
                        }
                    },
                    text = {
                        Text(text = alertText, style = MaterialTheme.typography.subtitle1)
                    },
                    buttons = {
                        Row(
                            modifier = Modifier.padding(all = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            TextButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = dismissAlert
                            ) {
                                Text("Dismiss")
                            }
                        }
                    }
                )
            }
        }
    }
}