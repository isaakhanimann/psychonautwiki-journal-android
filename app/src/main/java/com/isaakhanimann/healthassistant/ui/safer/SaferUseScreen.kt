package com.isaakhanimann.healthassistant.ui.safer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.SectionText
import com.isaakhanimann.healthassistant.ui.search.substance.VerticalSpace
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun SaferUsePreview() {
    SaferUseScreen(
        navigateToDrugTestingScreen = {},
        navigateToSaferHallucinogensScreen = {},
        navigateToVolumetricDosingScreen = {},
        navigateToDosageGuideScreen = {},
        navigateToDosageClassificationScreen = {},
        navigateToRouteExplanationScreen = {}
    )
}

@Composable
fun SaferUseScreen(
    navigateToDrugTestingScreen: () -> Unit,
    navigateToSaferHallucinogensScreen: () -> Unit,
    navigateToVolumetricDosingScreen: () -> Unit,
    navigateToDosageGuideScreen: () -> Unit,
    navigateToDosageClassificationScreen: () -> Unit,
    navigateToRouteExplanationScreen: () -> Unit
) {
    Scaffold(
        topBar = {
            JournalTopAppBar(title = "Safer Use")
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle(title = "1. Research")
            SectionText(text = "In advance research the duration, subjective effects and potential adverse effects which the substance or combination of substances are likely to produce.\nRead the info in here and also the PsychonautWiki article. Its best to cross-reference with other sources (Tripsit, Erowid, Wikipedia, Bluelight, Reddit, etc). There is no rush.")
            SectionTitle(title = "2. Testing")
            SectionText(text = "Test your substance with anonymous and free drug testing services. If those are not available in your country, use reagent testing kits. Times and producers change. Don‘t trust your dealer. Its better to have a tested stash instead of relying on a source spontaneously.")
            Button(
                onClick = navigateToDrugTestingScreen,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Text("Drug Testing Services")
            }
            val uriHandler = LocalUriHandler.current
            Button(
                onClick = {
                    uriHandler.openUri("https://dancesafe.org/testing-kit-instructions/")
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Icon(
                    Icons.Default.OpenInBrowser,
                    contentDescription = "Open Link",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Reagent Testing")
            }
            VerticalSpace()
            SectionTitle(title = "3. Dosage")
            SectionText(text = "You can always take more, but you can’t take less. Know your dose, start small and wait. A full stomach can delay the onset of a swallowed ingestion by hours. A dose that's easy for somebody with a tolerance might be too much for you.\n\nInvest in a milligram scale so you can accurately weigh your dosages. Bear in mind that milligram scales under ${'$'}1000 cannot accurately weigh out doses below 50 mg and are highly inaccurate under 10 - 15 mg. If the amounts of the drug are smaller, use volumetric dosing (dissolving in water or alcohol to make it easier to measure).\n\nMany substances do not have linear dose-response curves, meaning that doubling the dose amount will cause a greater than double increase (and rapidly result in overwhelming, unpleasant, and potentially dangerous experiences), therefore doses should only be adjusted upward with slight increases (e.g. 1/4 to 1/2 of the previous dose).")
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
            SectionTitle(title = "4. Set and Setting")
            SectionText(text = "Set: Make sure your thoughts, desires, feelings, general mood, and any preconceived notions or expectations about what you are about to experience are conducive to the experience. Make sure your body is well. Better not to take it if you feel sick, injured or generally unhealthy.\n\nSetting: An unfamiliar, uncontrollable or otherwise disagreeable social or physical environment may result in an unpleasant or dangerous experience. Choose an environment that provides a sense of safety, familiarity, control, and comfort. For using hallucinogens (psychedelics, dissociatives and deliriants) refer to the safer hallucinogen guide.")
            Button(
                onClick = navigateToSaferHallucinogensScreen,
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Text("Safer Hallucinogen Guide")
            }
            VerticalSpace()
            SectionTitle(title = "5. Combinations")
            SectionText(text = "Don’t combine drugs, including Alcohol, without research on the combo. The most common cause of substance-related deaths is the combination of depressants (such as opiates, benzodiazepines, or alcohol) with other depressants.")
            SectionTitle(title = "6. Administration Routes")
            SectionText(text = "Don’t share snorting equipment (straws, banknotes, bullets) to avoid blood-borne diseases such as Hepatitis C that can be transmitted through blood amounts so small you can’t notice. Injection is the the most dangerous route of administration and highly advised against. If you are determined to inject, don’t share injection materials and refer to the safer injection guide.")
            Button(
                onClick = {
                    uriHandler.openUri("https://www.youtube.com/watch?v=31fuvYXxeV0&list=PLkC348-BeCu6Ut-iJy8xp9_LLKXoMMroR")
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Icon(
                    Icons.Default.OpenInBrowser,
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
                    Icons.Default.OpenInBrowser,
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
                    Icons.Default.OpenInBrowser,
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
                Text("Administration Routes Info")
            }
            VerticalSpace()
            SectionTitle(title = "7. Allergy Tests")
            SectionText(text = "Simply dose a minuscule amount of the substance (e.g. 1/10 to 1/4 of a regular dose) and wait several hours to verify that you do not exhibit an unusual or idiosyncratic response.")
            SectionTitle(title = "8. Reflection")
            SectionText(text = "Carefully monitor the frequency and intensity of any substance use to ensure it is not sliding into abuse and addiction. In particular, many stimulants, opioids, and depressants are known to be highly addictive.")
            SectionTitle(title = "9. Safety of Others")
            SectionText(text = "Don’t drive, operate heavy machinery, or otherwise be directly or indirectly responsible for the safety or care of another person while intoxicated.")
            SectionTitle(title = "10. Recovery Position")
            SectionText(text = "If someone is unconscious and breathing place them into Recovery Position to prevent death by the suffocation of vomit after a drug overdose.\nHave the contact details of help services to hand in case of urgent need. ")
            Button(
                onClick = {
                    uriHandler.openUri("https://www.youtube.com/watch?v=dv3agW-DZ5I")
                },
                modifier = Modifier.padding(horizontal = horizontalPadding)
            ) {
                Icon(
                    Icons.Default.OpenInBrowser,
                    contentDescription = "Open Link",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Recovery Position Video")
            }
            VerticalSpace()
        }
    }
}