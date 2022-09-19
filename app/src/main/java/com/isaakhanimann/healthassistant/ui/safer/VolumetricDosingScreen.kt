package com.isaakhanimann.healthassistant.ui.safer

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
import com.isaakhanimann.healthassistant.ui.search.substance.ArticleLink

@Preview
@Composable
fun VolumetricDosingScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Volumetric Liquid Dosing")
                },
                actions = {
                    ArticleLink(url = "https://psychonautwiki.org/wiki/Volumetric_liquid_dosing")
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
                text = """Volumetric dosing is the process of dissolving a compound in a liquid to make it easier to measure. In the interest of harm reduction, it is important to use volumetric dosing with certain compounds that are too potent to measure with traditional weighing scales.
Many psychoactive substances, including benzodiazepines and certain psychedelics, are active at less than a single milligram. Such small quantities cannot be accurately measured with common digital scales, so the substance must instead be dosed volumetrically by weighing out larger amounts of the compound and dissolving it in a calculated volume of a suitable liquid.

Search the internet to determine what solvent to use. All substances should dissolve in alcohol, but many substances will not dissolve in water."""
            )
        }
    }
}