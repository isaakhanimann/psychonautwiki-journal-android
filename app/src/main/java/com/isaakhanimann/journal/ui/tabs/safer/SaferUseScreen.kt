/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.safer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.Biotech
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.search.substance.SectionWithTitle
import com.isaakhanimann.journal.ui.tabs.search.substance.VerticalSpace
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.theme.verticalPaddingCards

@Preview
@Composable
fun SaferUsePreview() {
    SaferUseScreen(
        navigateToDrugTestingScreen = {},
        navigateToSaferHallucinogensScreen = {},
        navigateToVolumetricDosingScreen = {},
        navigateToDosageGuideScreen = {},
        navigateToDosageClassificationScreen = {},
        navigateToRouteExplanationScreen = {},
        navigateToURL = {},
        navigateToReagentTestingScreen = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaferUseScreen(
    navigateToDrugTestingScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToDosageGuideScreen: () -> Unit,
    navigateToDosageClassificationScreen: () -> Unit,
    navigateToRouteExplanationScreen: () -> Unit,
    navigateToURL: (url: String) -> Unit,
    navigateToReagentTestingScreen: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Safer Use") },
            )
        }
    ) { padding ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            SectionWithTitle(title = "1. Research") {
                SaferText(text = "In advance research the duration, subjective effects and potential adverse effects which the substance or combination of substances are likely to produce.\nRead the info in here and also the PsychonautWiki article. Its best to cross-reference with other sources (Tripsit, Erowid, Wikipedia, Bluelight, Reddit, etc). There is no rush.")
            }
            val uriHandler = LocalUriHandler.current
            SectionWithTitle(title = "2. Testing") {
                SaferText(text = "Test your substance with anonymous and free drug testing services. If those are not available in your country, use reagent testing kits. Don‘t trust your dealer to sell reliable product. Its better to have a tested stash instead of relying on a source spontaneously.")
                Button(
                    onClick = navigateToDrugTestingScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Outlined.Biotech,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Drug Testing Services")
                }
                Button(
                    onClick = navigateToReagentTestingScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Outlined.Science,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Reagent Testing")
                }
                VerticalSpace()
            }
            SectionWithTitle(title = "3. Dosage") {
                SaferText(text = "Know your dose, start small and wait. A full stomach can delay the onset of a swallowed ingestion by hours. A dose that's easy for somebody with a tolerance might be too much for you.\n\nInvest in a milligram scale so you can accurately weigh your dosages. Bear in mind that milligram scales under ${'$'}1000 cannot accurately weigh out doses below 50 mg and are highly inaccurate under 10 - 15 mg. If the amounts of the drug are smaller, use volumetric dosing (dissolving in water or alcohol to make it easier to measure).\n\nMany substances do not have linear dose-response curves, meaning that doubling the dose amount will cause a greater than double increase (and rapidly result in overwhelming, unpleasant, and potentially dangerous experiences), therefore doses should only be adjusted upward with slight increases (e.g. 1/4 to 1/2 of the previous dose).")
                Button(
                    onClick = navigateToDosageGuideScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text("Dosage Guide")
                }
                Button(
                    onClick = navigateToDosageClassificationScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text("Dosage Classification")
                }
                Button(
                    onClick = navigateToVolumetricDosingScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text("Volumetric Liquid Dosing")
                }
                VerticalSpace()
            }
            SectionWithTitle(title = "4. Set and Setting") {
                SaferText(text = "Set: Make sure your thoughts, desires, feelings, general mood, and any preconceived notions or expectations about what you are about to experience are conducive to the experience. Make sure your body is well. Better not to take it if you feel sick, injured or generally unhealthy.\n\nSetting: An unfamiliar, uncontrollable or otherwise disagreeable social or physical environment may result in an unpleasant or dangerous experience. Choose an environment that provides a sense of safety, familiarity, control, and comfort. For using hallucinogens (psychedelics, dissociatives and deliriants) refer to the safer hallucinogen guide.")
                Button(
                    onClick = navigateToSaferHallucinogensScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Text("Safer Hallucinogen Guide")
                }
                VerticalSpace()
            }
            SectionWithTitle(title = "5. Combinations") {
                SaferText(text = "Don’t combine drugs, including Alcohol, without research on the combo. The most common cause of substance-related deaths is the combination of depressants (such as opiates, benzodiazepines, or alcohol) with other depressants.")
                Button(
                    onClick = {
                        uriHandler.openUri("https://combi-checker.ch")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Swiss Combination Checker")
                }
                Button(
                    onClick = {
                        uriHandler.openUri("https://combo.tripsit.me")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Tripsit Combination Checker")
                }
                VerticalSpace()
            }
            SectionWithTitle(title = "6. Administration Routes") {
                SaferText(text = "Don’t share snorting equipment (straws, banknotes, bullets) to avoid blood-borne diseases such as Hepatitis C that can be transmitted through blood amounts so small you can’t notice. Injection is the the most dangerous route of administration and highly advised against. If you are determined to inject, don’t share injection materials and refer to the safer injection guide.")
                Button(
                    onClick = {
                        uriHandler.openUri("https://www.youtube.com/watch?v=31fuvYXxeV0&list=PLkC348-BeCu6Ut-iJy8xp9_LLKXoMMroR")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Safer Snorting Video")
                }
                Button(
                    onClick = {
                        uriHandler.openUri("https://www.youtube.com/watch?v=lBlS2e46CV0&list=PLkC348-BeCu6Ut-iJy8xp9_LLKXoMMroR")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Safer Smoking Video")
                }
                Button(
                    onClick = {
                        uriHandler.openUri("https://www.youtube.com/watch?v=N7HjCPz4A7Y&list=PLkC348-BeCu6Ut-iJy8xp9_LLKXoMMroR")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Safer Injecting Video")
                }
                Button(
                    onClick = navigateToRouteExplanationScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Info",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Administration Routes Info")
                }
                VerticalSpace()
            }
            SectionWithTitle(title = "7. Allergy Tests") {
                SaferText(text = "Simply dose a minuscule amount of the substance (e.g. 1/10 to 1/4 of a regular dose) and wait several hours to verify that you do not exhibit an unusual or idiosyncratic response.")
            }
            SectionWithTitle(title = "8. Reflection") {
                SaferText(text = "Carefully monitor the frequency and intensity of any substance use to ensure it is not sliding into abuse and addiction. In particular, many stimulants, opioids, and depressants are known to be highly addictive.")
            }
            SectionWithTitle(title = "9. Safety of Others") {
                SaferText(text = "Don’t drive, operate heavy machinery, or otherwise be directly or indirectly responsible for the safety or care of another person while intoxicated.")
            }
            SectionWithTitle(title = "10. Recovery Position") {
                SaferText(text = "If someone is unconscious and breathing place them into Recovery Position to prevent death by the suffocation of vomit after a drug overdose.\nHave the contact details of help services to hand in case of urgent need.")
                Button(
                    onClick = {
                        uriHandler.openUri("https://www.youtube.com/watch?v=dv3agW-DZ5I")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Open Link",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Recovery Position Video")
                }
                VerticalSpace()
            }
            ElevatedCard(
                modifier = Modifier.padding(
                    horizontal = horizontalPadding,
                    vertical = verticalPaddingCards
                )
            ) {
                TextButton(
                    onClick = {
                        navigateToURL("https://psychonautwiki.org/wiki/Responsible_drug_use")
                    },
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                ) {
                    Icon(
                        Icons.Outlined.HealthAndSafety,
                        contentDescription = "Responsible Drug Use",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Responsible Drug Use Article")
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun SaferText(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Left,
        modifier = Modifier
            .padding(horizontal = horizontalPadding)
            .padding(bottom = 10.dp)
    )
}