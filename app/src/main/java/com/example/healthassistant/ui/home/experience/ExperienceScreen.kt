package com.example.healthassistant.ui.home.experience

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.ui.main.routers.navigateToAddIngestion

@Composable
fun ExperienceScreen(
    navController: NavHostController,
    viewModel: ExperienceViewModel = hiltViewModel()
) {
    viewModel.experienceWithIngestions?.let { exp ->
        ExperienceScreenContent(
            expAndIng = exp,
            navigateBack = navController::popBackStack,
            addIngestion = {
                navController.navigateToAddIngestion(substanceName = "MDMA", experienceId = exp.experience.id)
            }
        )
    } ?: run {
        Button(onClick = navController::popBackStack) {
            Text("There was an error. Go back.")
        }
    }
}


@Composable
fun ExperienceScreenContent(
    expAndIng: ExperienceWithIngestions,
    navigateBack: () -> Unit,
    addIngestion: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(expAndIng.experience.title) },
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
            FloatingActionButton(onClick = addIngestion) {
                Icon(Icons.Default.Add, "Add Ingestion")
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Ingestions")
            expAndIng.ingestions.forEach {
                IngestionRow(ingestion = it)
            }
        }
    }
}