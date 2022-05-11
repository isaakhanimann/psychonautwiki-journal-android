package com.example.healthassistant.presentation.search.substance

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
import com.example.healthassistant.ui.theme.HealthAssistantTheme
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

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun SubstanceScreenPreview(
    @PreviewParameter(SampleSubstanceProvider::class) substance: Substance,
    navigateBack: () -> Unit = {}
) {
    HealthAssistantTheme {
        SubstanceScreenContent(substance = substance, navigateBack = {})
    }
}

@Composable
fun SubstanceScreenContent(
    substance: Substance,
    navigateBack: () -> Unit
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
            val tolerance = substance.tolerance
            if (tolerance != null) {
                SubstanceInfoCard(title = "Tolerance") {
                    Column {
                        Text(text = "zero: ${tolerance.zero}")
                        Text(text = "half: ${tolerance.half}")
                        Text(text = "full: ${tolerance.full}")
                    }
                }
            }
            if (substance.crossTolerances.isNotEmpty()) {
                SubstanceInfoCard(title = "Cross Tolerance") {
                    Column {
                        substance.crossTolerances.forEach {
                            Text(text = it)
                        }
                    }
                }
            }
            if (substance.psychoactiveClasses.isNotEmpty()) {
                SubstanceInfoCard(title = "Psychoactive Class") {
                    Column {
                        substance.psychoactiveClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
            }
            if (substance.chemicalClasses.isNotEmpty()) {
                SubstanceInfoCard(title = "Chemical Class") {
                    Column {
                        substance.chemicalClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
            }
            if (substance.toxicity != null) {
                SubstanceInfoCard(title = "Toxicity") {
                    Text(text = substance.toxicity)
                }
            }
            if (substance.addictionPotential != null) {
                SubstanceInfoCard(title = "Addiction Potential") {
                    Text(substance.addictionPotential)
                }
            }
            if (substance.effects.isNotEmpty()) {
                SubstanceInfoCard(title = "Subjective Effects") {
                    FlowRow {
                        substance.effects.forEach {
                            EffectChip(effect = it)
                        }
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