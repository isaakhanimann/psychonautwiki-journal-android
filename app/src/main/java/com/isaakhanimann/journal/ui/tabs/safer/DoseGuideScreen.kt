/*
 * Copyright (c) 2022. Isaak Hanimann.
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.tabs.search.substance.SectionText
import com.isaakhanimann.journal.ui.theme.horizontalPadding

@Preview
@Composable
fun DoseGuideScreenPreview() {
    DoseGuideScreen(
        navigateToDoseClassification = {},
        navigateToVolumetricDosing = {},
        navigateToPWDosageArticle = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoseGuideScreen(
    navigateToDoseClassification: () -> Unit,
    navigateToVolumetricDosing: () -> Unit,
    navigateToPWDosageArticle: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Dosage Guide") })
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToPWDosageArticle,
                icon = {
                    Icon(
                        Icons.Outlined.Newspaper,
                        contentDescription = "Open Link"
                    )
                },
                text = { Text("Article") },
            )
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
                text = """Dosage refers to the size or amount of an administered psychoactive substance.
While scales can vary, it can generally be divided into six levels: threshold, light, common, strong, heavy, and overdose (fatal and non-fatal). Each of these levels are associated with a different degree of desired effects and potential side effects, depending on the substance and the individual's physiology.
In the context of psychonautic or recreational substance use, it is important to understand how dosages work.
Administering the wrong dosage of a substance can lead to negative experiences such as extreme anxiety, uncomfortable physical side effects, hospitalization, or (in extreme cases) death. Taking too low of a dose can often make people feel uncomfortable and frustrated, which can be dangerous when it leads them to re-dose at higher than the original intended amount, which can result in suddenly intense, overwhelming and potentially dangerous experiences."""
            )
            val titleStyle = MaterialTheme.typography.titleMedium
            Text("Choosing the Dosage", style = titleStyle)
            SectionText(
                text = """The user should avoid dosages that they are uncomfortable or unfamiliar with. An inexperienced user should always start at a lower dosage with the goal of working their way up to a normal or common dose. This allows the user to verify that their bodies are compatible with the substance and do not exhibit any allergies or unusual sensitivity.
After the initial test, the user may decide to increase the dosage. It is advised to increase the amount in controlled increments, usually adding 1/3 to 1/2 the previous dose. However, some substances are notable tricky to work with. For example, 2C-P is known to have a steep dose-response curve, as little as a few extra milligrams separating a manageable dose and an overdose. Also, 2C-P can have a very slow onset if ingested, and peak effects reportedly do not occur for 3 to 5 hours. So please be patient with new substances! The dose should never be doubled or tripled as this vastly increases the likelihood of either a bad trip or side effects such as anxiety, panic attacks, and psychosis.
This minimizes the risk of an accidental negative experience enormously; it is important to remember that everybody reacts differently to every substance depending on factors such as their own personal tolerance, brain chemistry, body weight, metabolism, stomach contents, and personal sensitivity. Another factor to consider is substance purity which can differ between batches of product.
For information on the appropriate dosage for any substance, research should be done using a combination of PsychonautWiki, Erowid, Tripsit, Wikipedia, Bluelight, and Google. If contradictory information about dosage is found, one should always start from the lowest in order minimize the risk."""
            )
            Text("Allergy Testing", style = titleStyle)
            SectionText(
                text = """Some people, especially those with health issues or over-reactive immune systems, may have adverse or allergic reactions to substances such as uncomfortable physical or cognitive effects, or over-sensitivity. The best way to prevent this is to perform an allergy test with every new batch of a substance the user receives (even if coming from a reliable source, as sometimes much more potent and dangerous compounds can become unintentionally mixed up with the product being advertised).
To properly perform an allergy test, one can simply dose a minuscule amount of the substance (1/6 of a regular dose) and then wait several hours to a few days.
When performing an allergy test, one should note any unusual side effects including rashes, hives, breathing discomfort, an uncomfortably fast heartbeat, or a burning sensation on one's skin. Depending on the severity of symptoms, medical services may be needed."""
            )
            Text("Dosage Measurement", style = titleStyle)
            SectionText(text = """There are various ways in which users measure their dosages of psychoactive substances. The most accurate and safe method to measure one's drugs is to use a combination of a milligram scale and the volumetric liquid dosing method. This is a vastly far safer route than "eyeballing" one's dosages by approximating the weight of what they are ingesting simply by looking at it and making a rough (but inaccurate) guess.""")
            Text("Eyeballing", style = titleStyle)
            SectionText(
                text = """Eyeballing is strongly discouraged in any context. Eyeballing is a highly inaccurate method of measuring substances which involves looking at a substance and making a rough guess of the weight. Since there is not much difference between 10mg and 30mg visually, it can result in one taking too much of a substance. There are numerous forms of eyeballing methods, including using micro scoops and the graph paper method.
In the graph paper method, a known quantity of a substance is spread evenly onto a piece of graph paper and one's doses are based on how many graph paper squares are covered. This method is inaccurate simply because the volume of a measured weight of powder can vary significantly in its density, even with different batches of the same substance. For example, two equal-sized piles of mescaline can have completely different densities and therefore different weights.
Users should not trust a vendor's word that their product weighs a certain amount as it is not unheard of for vendors to accidentally or intentionally give out the wrong amount of a product (both more than advertised or less), which can result in overdoses for some users.

It is especially dangerous to eyeball substances that are active at extremely low doses (under 10mg) and have sensitive dose-response curves.""".trimMargin()
            )
            Text("Milligram Scales", style = titleStyle)
            SectionText(
                text = """Milligram scales under ${'$'}1000 cannot accurately weigh out doses below at least 50mg and are highly inaccurate under 10-15mg, for more accurate readability add the included calibration weight to the scale without taring before you add a 10+ mg dose. For more potent drugs, users are strongly advised to use volumetric liquid dosing.
When buying chemicals in powdered form, it is strongly recommended that one invest in a reliable and accurate digital milligram scale to ensure that they are ingesting a safe and recommended dosage. A simple ${'$'}20-30 investment in such equipment can be the difference between a safe and enjoyable experience, and a negative and dangerous one.
To achieve the most accurate measurement, the scale should only be used on a completely flat surface away from vibrations, wind, and drafts. Low batteries can also affect the scale's accuracy, so one should occasionally replace them.
There are a huge variety of different scales available online through various sites. To choose a quality scale, users should read the reviews before buying and come to their own personal decision. Users should ideally choose a scale that has a 0.001 g readability. When using a scale that has 0.005 gram (5 mg) accuracy, the weight will be off by 5 mg in either direction, meaning that if one measures 20mg of a substance, the results will be between 15-25 mg.""".trimMargin()
            )
            Text("Weighing Technique", style = titleStyle)
            SectionText(
                text = """Most milligram scales are more accurate in higher ranges (5 - 15 grams) than the lower ranges. Therefore, it is better to weigh one's drug while the included calibration weight is on the scale. The following steps outline the weighing method for the most accurate measurement:
1. Place scale on completely flat surface away from wind and vibrations
2. Calibrate scale
3. Add the calibration weight that comes with the scale onto scale without tarring
4. Add empty gel capsule and mark down weight without tarring
5. Add the powder into the gel capsule
6. Mark down weight of filled capsule
7. Subtract the weight in step 4 from the final weight in step 6
8. Use volumetric liquid dosing afterwards, especially for very potent drugs (under 10mg)""".trimMargin()
            )
            Text("Volumetric Liquid Dosing", style = titleStyle)
            SectionText(
                text = """Volumetric liquid dosing should preferably be used for potent drugs because most standard milligram scales cannot accurately weigh out doses below 10-15mg.
Volumetric liquid dosing is the process of dissolving a known quantity of a compound in a liquid medium in order to make it easier to measure and accurately dose. In the interest of harm reduction, it is essential to prepare certain compounds which are too potent to measure with traditional weighing scales within a liquid solution.""".trimMargin()
            )
            Button(
                onClick = navigateToVolumetricDosing,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Text(text = "Volumetric Liquid Dosing")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text("Dosage Classification", style = titleStyle)
            SectionText(
                text = """The range and intensity of the effects of a substance depends on upon a number of factors. These include route of administration, dosage, set and setting, and personal and environmental factors.
Effective doses can be divided into five categories: threshold, light, common, strong, and heavy.""".trimMargin()
            )
            Button(
                onClick = navigateToDoseClassification,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Text(text = "Dosage Classification")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}