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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Biotech
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.theme.horizontalPadding
import com.isaakhanimann.journal.ui.theme.minimumTouchTargetHeight

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun DrugTestingScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Drug testing services") })
        }
    ) { padding ->
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = horizontalPadding)
        ) {
            CardWithTesting(title = "Austria") {
                TestingServiceItem(
                    name = "Drogenarbeit Z6",
                    city = "Innsbruck",
                    url = "https://www.drogenarbeitz6.at/drug-checking.html"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "Checkit!",
                    city = "Vienna",
                    url = "https://checkit.wien/drug-checking-2/"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "Triptalks",
                    city = "Graz",
                    url = "https://triptalks.at"
                )
            }
            CardWithTesting(title = "Australia") {
                TestingServiceItem(
                    name = "CanTEST",
                    city = "Canberra",
                    url = "https://www.cahma.org.au/services/cantest/"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "CheQpoint",
                    city = "Queensland",
                    url = "https://www.quihn.org/cheqpoint/"
                )
            }
            CardWithTesting(title = "Belgium") {
                TestingServiceItem(
                    name = "Modus Vivendi",
                    city = "Saint-Gilles",
                    url = "https://www.modusvivendi-be.org"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "Exaequo @ Rainbowhouse",
                    city = "Brussels",
                    url = "https://www.exaequo.be"
                )
            }
            CardWithTesting(title = "Canada") {
                TestingServiceItem(
                    name = "Get Your Drugs Tested",
                    city = "Vancouver",
                    url = "http://www.vch.ca/public-health/harm-reduction/overdose-prevention-response/drug-checking"
                )
            }
            CardWithTesting(title = "Germany") {
                TestingServiceItem(
                    name = "Drugchecking",
                    city = "Berlin",
                    url = "https://drugchecking.berlin"
                )
            }
            CardWithTesting(title = "France") {
                TestingServiceItem(
                    name = "Asso Michel - CAARUD Médiane",
                    city = "Dunkerque",
                    url = "https://www.associationmichel.com/caarud-mediane-722/le-caarud-mediane-743/"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "Le MAS - CAARUD Pause diabolo",
                    city = "Lyon",
                    url = "https://www.mas-asso.fr/service/pause-diabolo/"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "Centre \"Les Wads\"",
                    city = "Metz",
                    url = "http://www.leswadscmsea.fr"
                )
            }
            CardWithTesting(title = "Italy") {
                TestingServiceItem(
                    name = "Neutravel Project",
                    city = "Torino",
                    url = "https://www.neutravel.net/drug-checking"
                )
            }
            CardWithTesting(title = "Netherlands") {
                TestingServiceItem(
                    name = "Drugs-test",
                    city = "33 locations",
                    url = "https://www.drugs-test.nl/en/testlocations/"
                )
            }
            CardWithTesting(title = "Slovenia") {
                TestingServiceItem(
                    name = "DrogArt",
                    city = "Various locations",
                    url = "https://www.drogart.org/testirne-tocke/"
                )
            }
            CardWithTesting(title = "Spain") {
                TestingServiceItem(
                    name = "Energy Control",
                    city = "Various locations",
                    url = "https://energycontrol.org/servicio-de-analisis/"
                )
                TestingServiceItem(
                    name = "Kykeon Analytics",
                    city = "Various locations",
                    url = "https://www.kykeonanalytics.com"
                )
                TestingServiceItem(
                    name = "Ai Laket",
                    city = "Vitoria-Gasteiz, Bilbo and Donosti",
                    url = "https://ailaket.com/proyectos/punto-fijo/"
                )
            }
            CardWithTesting(title = "Switzerland") {
                TestingServiceItem(
                    name = "DIBS / Safer Dance Basel",
                    city = "Basel",
                    url = "https://de.saferdancebasel.ch/drugchecking"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "DIB / rave it safe",
                    city = "Bern, Biel",
                    url = "https://www.raveitsafe.ch/angebotsdetails/dib-drug-checking-bern/"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "Nuit Blanche",
                    city = "Geneva",
                    url = "https://nuit-blanche.ch/drug-checking/"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "DIZ / Saferparty",
                    city = "Zurich",
                    url = "https://en.saferparty.ch/angebote/drug-checking"
                )
                HorizontalDivider()
                TestingServiceItem(
                    name = "DILU Luzern",
                    city = "Luzern",
                    url = "https://www.gassenarbeit.ch/angebote/dilu"
                )
            }

            CardWithTesting(title = "United Kingdom") {
                TestingServiceItem(
                    name = "The Loop",
                    city = "Bristol",
                    url = "https://wearetheloop.org"
                )
            }
            val uriHandler = LocalUriHandler.current
            val reportServiceText = "Report missing service"
            TextButton(
                onClick = {
                    uriHandler.openUri("https://t.me/isaakhanimann")
                },
                modifier = Modifier.semantics {
                    contentDescription = reportServiceText
                }) {
                Text(
                    reportServiceText,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Composable
fun CardWithTesting(
    title: String,
    content: @Composable () -> Unit
) {
    ElevatedCard(modifier = Modifier.padding(vertical = 5.dp)) {
        Column(
            Modifier.padding(vertical = 5.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            HorizontalDivider()
            content()
        }
    }
}

@Preview
@Composable
fun TestingServiceItemPreview() {
    TestingServiceItem(
        name = "DIZ / Saferparty",
        city = "Zurich",
        url = "https://en.saferparty.ch/angebote/drug-checking"
    )
}

@Composable
fun TestingServiceItem(
    name: String,
    city: String,
    url: String
) {
    val uriHandler = LocalUriHandler.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .clickable {
                uriHandler.openUri(url)
            }
            .padding(horizontal = horizontalPadding, vertical = 5.dp)
            .heightIn(min = minimumTouchTargetHeight)
            .fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Outlined.Biotech,
            contentDescription = "Open Link"
        )
        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = city,
            maxLines = 1,
            modifier = Modifier.width(IntrinsicSize.Max)
        )
    }
}

