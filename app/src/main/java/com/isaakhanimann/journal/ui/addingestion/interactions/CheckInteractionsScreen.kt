package com.isaakhanimann.journal.ui.addingestion.interactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.journal.data.substances.classes.InteractionType
import com.isaakhanimann.journal.data.substances.classes.SubstanceWithCategories
import com.isaakhanimann.journal.ui.search.substance.InteractionExplanationButton
import com.isaakhanimann.journal.ui.search.substance.SubstanceWithCategoriesPreviewProvider
import com.isaakhanimann.journal.ui.theme.horizontalPadding


@Composable
fun CheckInteractionsScreen(
    navigateToNext: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    CheckInteractionsScreen(
        substanceName = viewModel.substanceName,
        substanceUrl = viewModel.substance.url,
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
        navigateToNext = navigateToNext,
        navigateToURL = navigateToURL,
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
        substanceUrl = "",
        isSearchingForInteractions = true,
        dangerousInteractions = substanceWithCategories.substance.interactions?.dangerous
            ?: emptyList(),
        unsafeInteractions = substanceWithCategories.substance.interactions?.unsafe ?: emptyList(),
        uncertainInteractions = substanceWithCategories.substance.interactions?.uncertain
            ?: emptyList(),
        navigateToNext = {},
        navigateToURL = {},
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
        substanceUrl = "",
        isSearchingForInteractions = true,
        dangerousInteractions = emptyList(),
        unsafeInteractions = emptyList(),
        uncertainInteractions = emptyList(),
        navigateToNext = {},
        navigateToURL = {},
        dismissAlert = {},
        isShowingAlert = true,
        alertInteractionType = InteractionType.DANGEROUS,
        alertText = "Dangerous Interaction with Heroin taken 4h ago."
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInteractionsScreen(
    substanceName: String,
    substanceUrl: String,
    isSearchingForInteractions: Boolean,
    isShowingAlert: Boolean,
    dismissAlert: () -> Unit,
    alertInteractionType: InteractionType?,
    alertText: String,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
    navigateToNext: () -> Unit,
    navigateToURL: (url: String) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("$substanceName Interactions") }) },
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
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (isSearchingForInteractions) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            } else {
                LinearProgressIndicator(progress = 0.33f, modifier = Modifier.fillMaxWidth())
            }
            if (dangerousInteractions.isEmpty() && unsafeInteractions.isEmpty() && uncertainInteractions.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No interactions found... check other sources.")
                }
            } else {
                val verticalSpaceBetween = 1.dp
                val verticalPaddingInside = 4.dp
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    if (dangerousInteractions.isNotEmpty()) {
                        items(dangerousInteractions) {
                            InteractionRow(
                                text = it,
                                interactionType = InteractionType.DANGEROUS,
                                verticalSpaceBetween = verticalSpaceBetween,
                                verticalPaddingInside = verticalPaddingInside,
                            )
                        }
                    }
                    if (unsafeInteractions.isNotEmpty()) {
                        items(unsafeInteractions) {
                            InteractionRow(
                                text = it, interactionType = InteractionType.UNSAFE,
                                verticalSpaceBetween = verticalSpaceBetween,
                                verticalPaddingInside = verticalPaddingInside,
                            )
                        }
                    }
                    if (uncertainInteractions.isNotEmpty()) {
                        items(uncertainInteractions) {
                            InteractionRow(
                                text = it, interactionType = InteractionType.UNCERTAIN,
                                verticalSpaceBetween = verticalSpaceBetween,
                                verticalPaddingInside = verticalPaddingInside,
                            )
                        }
                    }
                    item {
                        InteractionExplanationButton(substanceURL = substanceUrl, navigateToURL = navigateToURL)
                    }
                }
            }
            if (isShowingAlert && alertInteractionType != null) {
                AlertDialog(
                    onDismissRequest = dismissAlert,
                    title = {
                        val title = when (alertInteractionType) {
                            InteractionType.DANGEROUS -> "Dangerous Interaction!"
                            InteractionType.UNSAFE -> "Unsafe Interaction"
                            InteractionType.UNCERTAIN -> "Uncertain Interaction"
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(text = title, style = MaterialTheme.typography.titleLarge)
                        }
                    },
                    text = {
                        Text(text = alertText)
                    },
                    confirmButton = {
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

@Composable
fun InteractionRow(
    text: String,
    interactionType: InteractionType,
    verticalSpaceBetween: Dp = 1.dp,
    verticalPaddingInside: Dp = 2.dp
) {
    Surface(
        modifier = Modifier
            .padding(vertical = verticalSpaceBetween)
            .fillMaxWidth(),
        shape = RectangleShape,
        color = interactionType.color
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
                vertical = verticalPaddingInside
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            LazyRow {
                items(interactionType.dangerCount) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = "Warning",
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}