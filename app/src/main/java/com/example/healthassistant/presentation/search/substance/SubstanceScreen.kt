package com.example.healthassistant.presentation.search.substance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.presentation.search.substance.roa.RoaView

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
fun SubstanceScreenContent(@PreviewParameter(SampleSubstanceProvider::class) substance: Substance, navigateBack: () -> Unit = {}) {
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
            modifier = Modifier.padding(horizontal = 10.dp)
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

            Text(substance.addictionPotential ?: "-")
        }
    }
}