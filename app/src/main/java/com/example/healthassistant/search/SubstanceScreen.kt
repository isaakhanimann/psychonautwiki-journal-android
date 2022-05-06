package com.example.healthassistant.search

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun SubstanceScreen(navHostController: NavHostController,substanceName: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(substanceName)},
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack()}
                    ) {
                        Icon(Icons.Default.ArrowBack, "backIcon")
                    }
                }
            )
        }
    ) {
        Text("Hello")
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    val navController = rememberNavController()
    SubstanceScreen(navHostController = navController,"MDMA")
}