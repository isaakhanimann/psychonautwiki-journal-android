package com.example.healthassistant.presentation.search.substance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthassistant.data.substances.Effect
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.presentation.search.substance.roa.RoaView
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun SubstanceScreen(navHostController: NavHostController) {
    val viewModel: SubstanceViewModel = hiltViewModel()
    if (viewModel.substance != null) {
        SubstanceScreenContent(
            substance = viewModel.substance,
            navigateBack = navHostController::popBackStack
        )
    } else {
        Button(onClick = navHostController::popBackStack) {
            Text("There was an error. Go back.")
        }
    }
}

@Preview
@Composable
fun SubstanceScreenContent(
    @PreviewParameter(SampleSubstanceProvider::class) substance: Substance,
    navigateBack: () -> Unit = {}
) {
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
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, "Ingest ${substance.name}")
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val uriHandler = LocalUriHandler.current
            Button(onClick = {
                uriHandler.openUri(substance.url)
            }) {
                Text("Read Article")
            }
            substance.roas.forEach { roa ->
                RoaView(roa)
            }
            InteractionsView(substance = substance)

            if (substance.psychoactiveClasses.isNotEmpty()) {
                Text(text = "Psychoactive Classes", style = MaterialTheme.typography.h5)
                substance.psychoactiveClasses.forEach {
                    Text(text = it, modifier = Modifier.padding(horizontal = 5.dp))
                }
            }
            if (substance.chemicalClasses.isNotEmpty()) {
                Text(text = "Chemical Classes", style = MaterialTheme.typography.h5)
                substance.chemicalClasses.forEach {
                    Text(text = it, modifier = Modifier.padding(horizontal = 5.dp))
                }
            }
            val tolerance = substance.tolerance
            if (tolerance != null) {
                Text(text = "Tolerance", style = MaterialTheme.typography.h5)
                Text(text = "zero: ${tolerance.zero}")
                Text(text = "half: ${tolerance.half}")
                Text(text = "full: ${tolerance.full}")
            }
            if (substance.crossTolerances.isNotEmpty()) {
                Text(text = "Cross Tolerances", style = MaterialTheme.typography.h5)
                substance.crossTolerances.forEach {
                    Text(text = it, modifier = Modifier.padding(horizontal = 5.dp))
                }
            }
            if (substance.toxicity != null) {
                Text(text = "Toxicity", style = MaterialTheme.typography.h5)
                Text(text = substance.toxicity)
            }
            if (substance.addictionPotential != null) {
                Text(text = "Addiction Potential", style = MaterialTheme.typography.h5)
                Text(substance.addictionPotential)
            }
            if (substance.effects.isNotEmpty()) {
                Text(text = "Subjective Effects", style = MaterialTheme.typography.h5)
                FlowRow {
                    substance.effects.forEach {
                        EffectChip(effect = it)
                    }
                }
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
        Row(Modifier.padding(horizontal = 10.dp, vertical = 3.dp)) {
            Icon(Icons.Default.ExitToApp, "link", tint = Color.White)
            Text(
                effect.name,
                color = Color.White
            )
        }

    }
}