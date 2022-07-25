package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SaferStimulantsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Safer Stimulants Use") },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = """
                Consider how long you want to stay awake. Don't suppress your need for sleep.
                
                Drink enough non-alcoholic drinks (3 - 5 dl per hour) and take breaks in the fresh air.
                
                Eat healthy before and after consumption and do not consume on an empty stomach.
                
                People with psychological disorders, pre-existing cardiovascular conditions, asthma, liver and kidney disorders or diabetes, hyperthyroidism and pregnant women are particulary discouraged from taking stimulants.
                
                Take vitamin C and D and minerals (iron, calcium and magnesium) with frequent use.
                
                It is better not to wear headgear (danger of overheating).
            """.trimIndent())
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}