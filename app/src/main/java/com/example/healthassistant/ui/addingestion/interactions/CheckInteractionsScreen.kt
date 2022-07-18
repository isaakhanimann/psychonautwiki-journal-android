package com.example.healthassistant.ui.addingestion.interactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.healthassistant.data.substances.InteractionType
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.search.substance.InteractionChip
import com.example.healthassistant.ui.search.substance.SubstancePreviewProvider
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun CheckInteractionsScreen(
    navigateToChooseRouteScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    CheckInteractionsScreen(
        substanceName = viewModel.substanceName,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        isShowingHallucinogenLink = viewModel.isShowingHallucinogenLink,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
        navigateToNext = navigateToChooseRouteScreen,
        navigateToSaferHallucinogensScreen = navigateToSaferHallucinogensScreen,
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
fun CheckInteractionsScreenPreview(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    CheckInteractionsScreen(
        substanceName = "LSD",
        isSearchingForInteractions = true,
        isShowingHallucinogenLink = true,
        dangerousInteractions = substance.dangerousInteractions,
        unsafeInteractions = substance.unsafeInteractions,
        uncertainInteractions = substance.uncertainInteractions,
        navigateToNext = {},
        navigateToSaferHallucinogensScreen = {},
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
        dangerousInteractions = emptyList(),
        unsafeInteractions = emptyList(),
        uncertainInteractions = emptyList(),
        navigateToNext = {},
        navigateToSaferHallucinogensScreen = {},
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
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (dangerousInteractions.isNotEmpty()) {
                        Text(text = "Dangerous Interactions", style = MaterialTheme.typography.h6)
                        FlowRow {
                            dangerousInteractions.forEach {
                                InteractionChip(text = it, color = InteractionType.DANGEROUS.color)
                            }
                        }
                    }
                    if (unsafeInteractions.isNotEmpty()) {
                        Text(text = "Unsafe Interactions", style = MaterialTheme.typography.h6)
                        FlowRow {
                            unsafeInteractions.forEach {
                                InteractionChip(text = it, color = InteractionType.UNSAFE.color)
                            }
                        }
                    }
                    if (uncertainInteractions.isNotEmpty()) {
                        Text(text = "Uncertain Interactions", style = MaterialTheme.typography.h6)
                        FlowRow {
                            uncertainInteractions.forEach {
                                InteractionChip(text = it, color = InteractionType.UNCERTAIN.color)
                            }
                        }
                    }
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