package com.example.healthassistant.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthassistant.data.substances.*

@Composable
fun SubstanceScreen(navHostController: NavHostController) {
    val viewModel: SubstanceViewModel = hiltViewModel()
    if (viewModel.substance != null) {
        SubstanceScreenContent(substance = viewModel.substance, navigateBack = navHostController::popBackStack)
    } else {
        Button(onClick = navHostController::popBackStack) {
            Text("There was an error. Go back.")
        }
    }
}

@Preview
@Composable
fun SubstanceScreenContent(substance: Substance = exampleSubstance, navigateBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(substance.name) },
                navigationIcon = {
                    IconButton(
                        onClick = navigateBack
                    ) {
                        Icon(Icons.Default.ArrowBack, "backIcon")
                    }
                }
            )
        }
    ) {
        Column {
            val uriHandler = LocalUriHandler.current
            Button(onClick = {
                uriHandler.openUri(substance.url)
            }) {
                Text("Read Article")
            }
            Text(substance.name)
            Text(substance.addictionPotential ?: "-")
        }
    }
}

val exampleSubstance = Substance(
    name = "Example Substance",
    url = "https://psychonautwiki.org/wiki/Lsd",
    effects = listOf(),
    chemicalClasses = listOf("Amphetamines"),
    psychoactiveClasses = listOf("Entactogens"),
    tolerance = Tolerance(
        full = "with prolonged and repeated use",
        half = "1 month",
        zero = "2 months"
    ),
    roas = listOf(
        Roa(
            name = "oral",
            roaDose = RoaDose(
                "mg",
                threshold = 20.0,
                light = DoseRange(
                    min = 20.0,
                    max = 40.0
                ),
                common = DoseRange(
                    min = 40.0,
                    max = 90.0
                ),
                strong = DoseRange(
                    min = 90.0,
                    max = 140.0
                ),
                heavy = 140.0
                ),
            roaDuration = RoaDuration(
                onset = DurationRange(
                    min = 20.0,
                    max = 40.0,
                    units = "minutes"
                ),
                comeup = DurationRange(
                    min = 15.0,
                    max = 30.0,
                    units = "minutes"
                ),
                peak = DurationRange(
                    min = 1.5,
                    max = 2.5,
                    units = "hours"
                ),
                offset = DurationRange(
                    min = 1.0,
                    max = 1.5,
                    units = "hours"
                ),
                total = DurationRange(
                    min = 3.0,
                    max = 5.0,
                    units = "hours"
                ),
                afterglow = DurationRange(
                    min = 12.0,
                    max = 48.0,
                    units = "hours"
                )
            ),
            bioavailability = Bioavailability(
                min = 70.0,
                max = 75.0
            )
        )
    ),
    addictionPotential = "moderately addictive with a high potential for abuse",
    toxicity = "toxic dose is unknown",
    crossTolerances = listOf(
        "Psychedelics",
        "Stimulants"
    ),
    uncertainInteractions = listOf(
        "MDMA",
        "Stimulants",
        "Dextromethorphan"
    ),
    unsafeInteractions = listOf(
        "Tramadol",
        "MAOI",
        "Dissociatives"
    ),
    dangerousInteractions = listOf(
        "Alcohol",
        "AMT",
        "Cocaine"
    )
)