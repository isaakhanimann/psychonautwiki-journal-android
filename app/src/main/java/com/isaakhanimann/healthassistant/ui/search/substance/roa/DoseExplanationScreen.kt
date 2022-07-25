package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.DoseClass

@Preview
@Composable
fun DoseExplanationScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Dosage Classification")
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "The range and intensity of the effects of a substance depends on upon a number of factors. These include route of administration, dosage, set and setting, and personal and environmental factors.\n" +
                        "Effective doses can be divided into five categories: threshold, light, common, strong, and heavy."
            )
            Spacer(modifier = Modifier.height(5.dp))
            DoseClass.values().forEach {
                Text(text = it.name, style = MaterialTheme.typography.h6)
                Text(text = it.description)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}