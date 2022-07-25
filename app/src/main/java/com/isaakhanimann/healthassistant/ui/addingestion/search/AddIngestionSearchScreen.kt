package com.isaakhanimann.healthassistant.ui.addingestion.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Launch
import androidx.compose.material.icons.outlined.Science
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.search.SearchContent

@Composable
fun AddIngestionSearchScreen(
    navigateToCheckInteractions: (substanceName: String) -> Unit,
    navigateToDrugTestingScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose Substance") }
            )
        }
    ) {
        Column {
            LinearProgressIndicator(progress = 0.17f, modifier = Modifier.fillMaxWidth())
            SearchContent(
                onSubstanceTap = {
                    navigateToCheckInteractions(it.name)
                },
                modifier = Modifier.weight(1f)
            )
            TestingSection(navigateToDrugTestingScreen)
        }
    }
}

@Preview
@Composable
fun TestingSectionPreview(
) {
    TestingSection(navigateToDrugTestingScreen = {})
}

@Composable
fun TestingSection(
    navigateToDrugTestingScreen: () -> Unit
) {
    var isVisible by remember {
        mutableStateOf(true)
    }
    AnimatedVisibility(visible = isVisible) {
        Card(
            elevation = 100.dp,
            backgroundColor = MaterialTheme.colors.primarySurface
        ) {
            Column(
                modifier = Modifier.padding(start = 10.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Test Your Substances!", style = MaterialTheme.typography.h6)
                    IconButton(onClick = { isVisible = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Testing Info"
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "If Available Use ")
                    TextButton(onClick = navigateToDrugTestingScreen) {
                        Icon(
                            Icons.Outlined.Science,
                            contentDescription = "Navigate to Testing Services",
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            tint = MaterialTheme.colors.secondary
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Drug Testing Services", color = MaterialTheme.colors.secondary)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Or At Least Use ")
                    val uriHandler = LocalUriHandler.current
                    TextButton(
                        onClick = {
                            uriHandler.openUri("https://dancesafe.org/testing-kit-instructions/")
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Launch,
                            contentDescription = "Open Link",
                            modifier = Modifier.size(ButtonDefaults.IconSize),
                            tint = MaterialTheme.colors.secondary
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text("Reagent Testing", color = MaterialTheme.colors.secondary)
                    }
                }
            }
        }
    }
}