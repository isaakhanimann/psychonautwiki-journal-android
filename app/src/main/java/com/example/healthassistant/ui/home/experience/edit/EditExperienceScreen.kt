package com.example.healthassistant.ui.home.experience.edit

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable

@Composable
fun EditExperienceScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Experience") }
            )
        }
    ) {
        Text(text = "Edit Text")
    }

}