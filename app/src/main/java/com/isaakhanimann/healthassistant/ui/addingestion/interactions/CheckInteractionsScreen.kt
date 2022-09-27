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
import com.isaakhanimann.healthassistant.data.substances.classes.SubstanceWithCategories
import com.isaakhanimann.healthassistant.ui.search.substance.InteractionRow
import com.isaakhanimann.healthassistant.ui.search.substance.SubstanceWithCategoriesPreviewProvider


@Composable
fun CheckInteractionsScreen(
    navigateToNext: () -> Unit,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    CheckInteractionsScreen(
        substanceName = viewModel.substanceName,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
        navigateToNext = navigateToNext,
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
fun CheckInteractionsScreenPreview(@PreviewParameter(SubstanceWithCategoriesPreviewProvider::class) substanceWithCategories: SubstanceWithCategories) {
    CheckInteractionsScreen(
        substanceName = "LSD",
        isSearchingForInteractions = true,
        dangerousInteractions = substanceWithCategories.substance.interactions?.dangerous
            ?: emptyList(),
        unsafeInteractions = substanceWithCategories.substance.interactions?.unsafe ?: emptyList(),
        uncertainInteractions = substanceWithCategories.substance.interactions?.uncertain
            ?: emptyList(),
        navigateToNext = {},
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
        dangerousInteractions = emptyList(),
        unsafeInteractions = emptyList(),
        uncertainInteractions = emptyList(),
        navigateToNext = {},
        dismissAlert = {},
        isShowingAlert = true,
        alertInteractionType = InteractionType.DANGEROUS,
        alertText = "Dangerous Interaction with Heroin taken 4h ago."
    )
}

@Composable
fun CheckInteractionsScreen(
    substanceName: String,
    isSearchingForInteractions: Boolean,
    isShowingAlert: Boolean,
    dismissAlert: () -> Unit,
    alertInteractionType: InteractionType?,
    alertText: String,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
    navigateToNext: () -> Unit
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
                val verticalSpaceBetween = 1.dp
                val verticalPaddingInside = 4.dp
                val style = MaterialTheme.typography.h6

                Column(modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)) {
                    if (dangerousInteractions.isNotEmpty()) {
                        dangerousInteractions.forEach {
                            InteractionRow(
                                text = it,
                                interactionType = InteractionType.DANGEROUS,
                                verticalSpaceBetween = verticalSpaceBetween,
                                verticalPaddingInside = verticalPaddingInside,
                                textStyle = style
                            )
                        }
                    }
                    if (unsafeInteractions.isNotEmpty()) {
                        unsafeInteractions.forEach {
                            InteractionRow(
                                text = it, interactionType = InteractionType.UNSAFE,
                                verticalSpaceBetween = verticalSpaceBetween,
                                verticalPaddingInside = verticalPaddingInside,
                                textStyle = style
                            )

                        }
                    }
                    if (uncertainInteractions.isNotEmpty()) {
                        uncertainInteractions.forEach {
                            InteractionRow(
                                text = it, interactionType = InteractionType.UNCERTAIN,
                                verticalSpaceBetween = verticalSpaceBetween,
                                verticalPaddingInside = verticalPaddingInside,
                                textStyle = style
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Check the PsychonautWiki article for explanations",
                        style = MaterialTheme.typography.caption,
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