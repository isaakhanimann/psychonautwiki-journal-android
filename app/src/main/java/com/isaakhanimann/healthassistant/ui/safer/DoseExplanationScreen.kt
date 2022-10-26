package com.isaakhanimann.healthassistant.ui.safer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.healthassistant.data.substances.classes.roa.DoseClass
import com.isaakhanimann.healthassistant.ui.search.substance.SectionText
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DoseExplanationScreen() {
    Scaffold(
        topBar = {
            JournalTopAppBar(title = "Dosage Classification")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            SectionText(
                text = "The range and intensity of the effects of a substance depends on upon a number of factors. These include route of administration, dosage, set and setting, and personal and environmental factors.\n" +
                        "Effective doses can be divided into five categories: threshold, light, common, strong, and heavy."
            )
            DoseClass.values().forEach {
                Text(text = it.name, style = MaterialTheme.typography.titleMedium)
                SectionText(it.description)
            }
        }
    }
}