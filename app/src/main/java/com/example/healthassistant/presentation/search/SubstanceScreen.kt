package com.example.healthassistant.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SubstanceScreen(navHostController: NavHostController) {
    val viewModel: SubstanceViewModel = hiltViewModel()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("hello") },
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack() }
                    ) {
                        Icon(Icons.Default.ArrowBack, "backIcon")
                    }
                }
            )
        }
    ) {
        viewModel.substance?.let { sub ->
            Column {
                Text(sub.name)
                Text(sub.addictionPotential ?: "-")
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    val navController = rememberNavController()
    SubstanceScreen(navHostController = navController)
}