package com.example.healthassistant.ui.addingestion.interactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider
import com.example.healthassistant.ui.search.substance.InteractionChip
import com.google.accompanist.flowlayout.FlowRow


@Composable
fun CheckInteractionsScreen(
    navigateToChooseRouteScreen: () -> Unit,
    viewModel: CheckInteractionsViewModel = hiltViewModel()
) {
    CheckInteractionsScreen(
        isSearchingForInteractions = viewModel.isSearchingForInteractions,
        dangerousInteractions = viewModel.dangerousInteractions,
        unsafeInteractions = viewModel.unsafeInteractions,
        uncertainInteractions = viewModel.uncertainInteractions,
        navigateToNext = navigateToChooseRouteScreen,
        dismissAlert = {
            viewModel.isShowingAlert = false
        },
        isShowingAlert = viewModel.isShowingAlert,
        alertTitle = viewModel.alertTitle,
        alertText = viewModel.alertText
    )
}

@Preview
@Composable
fun CheckInteractionsScreenPreview(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    CheckInteractionsScreen(
        isSearchingForInteractions = true,
        dangerousInteractions = substance.dangerousInteractions,
        unsafeInteractions = substance.unsafeInteractions,
        uncertainInteractions = substance.uncertainInteractions,
        navigateToNext = {},
        dismissAlert = {},
        isShowingAlert = true,
        alertTitle = "Dangerous Interaction Detected!",
        alertText = "Dangerous Interaction with Heroin taken 4h 10m ago"
    )
}

@Preview
@Composable
fun CheckInteractionsScreenPreview2() {
    CheckInteractionsScreen(
        isSearchingForInteractions = true,
        dangerousInteractions = emptyList(),
        unsafeInteractions = emptyList(),
        uncertainInteractions = emptyList(),
        navigateToNext = {},
        dismissAlert = {},
        isShowingAlert = false,
        alertTitle = "Dangerous Interaction Detected!",
        alertText = "Dangerous Interaction with Heroin taken 4h 10m ago"
    )
}

@Composable
fun CheckInteractionsScreen(
    isSearchingForInteractions: Boolean,
    isShowingAlert: Boolean,
    dismissAlert: () -> Unit,
    alertTitle: String,
    alertText: String,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
    navigateToNext: () -> Unit
) {
    Column {
        LinearProgressIndicator(progress = 0.33f, modifier = Modifier.fillMaxWidth())
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Check Interactions") }) },
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
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(1f)
                ) {
                    FlowRow(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 5.dp)
                            .weight(1f)
                    ) {
                        dangerousInteractions.forEach {
                            InteractionChip(text = it, color = Color.Red)
                        }
                        unsafeInteractions.forEach {
                            InteractionChip(text = it, color = Color(0xFFFF9800))
                        }
                        uncertainInteractions.forEach {
                            InteractionChip(text = it, color = Color.Yellow)
                        }
                    }
                }
            }
            if (isShowingAlert) {
                AlertDialog(
                    onDismissRequest = dismissAlert,
                    title = {
                        Text(text = alertTitle)
                    },
                    text = {
                        Text(text = alertText)
                    },
                    buttons = {
                        Row(
                            modifier = Modifier.padding(all = 8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
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