package com.isaakhanimann.healthassistant.ui.safer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun VolumetricDosingScreen() {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            JournalTopAppBar(
                title = "Volumetric Liquid Dosing"
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { uriHandler.openUri("https://psychonautwiki.org/wiki/Volumetric_liquid_dosing") },
                icon = {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link"
                    )
                },
                text = { Text("More Info") },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = horizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = """Volumetric dosing is the process of dissolving a compound in a liquid to make it easier to measure. In the interest of harm reduction, it is important to use volumetric dosing with certain compounds that are too potent to measure with traditional weighing scales.
Many psychoactive substances, including benzodiazepines and certain psychedelics, are active at less than a single milligram. Such small quantities cannot be accurately measured with common digital scales, so the substance must instead be dosed volumetrically by weighing out larger amounts of the compound and dissolving it in a calculated volume of a suitable liquid.

Search the internet to determine what solvent to use. All substances should dissolve in alcohol, but many substances will not dissolve in water.""",
                textAlign = TextAlign.Justify
            )
        }
    }
}