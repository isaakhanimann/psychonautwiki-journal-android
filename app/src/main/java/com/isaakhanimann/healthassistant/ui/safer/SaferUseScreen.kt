package com.isaakhanimann.healthassistant.ui.safer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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

@Preview
@Composable
fun SaferUseScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Safer Use") }
            )
        }
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {
            val titleStyle = MaterialTheme.typography.h6
            Text(text = "1. Research", style = titleStyle)
            Text(text = "Research the substance you intend to consume. Read the info in here and also the PsychonautWiki Article. Its best to cross-reference with other sources (Tripsit, Erowid, Wikipedia, Bluelight, Reddit, etc ). There is no rush.")
            Text(text = "2. Testing", style = titleStyle)
            Text(text = "Test your substance with anonymous and free drug testing services. If not available in your country use reagent testing kits. Times change. Producers change. Don‘t trust your dealer. Its better to have a tested stash instead of relying on a source spontaneously.")
            Text(text = "3. Dosage", style = titleStyle)
            Text(text = "You can always take more, but you can’t take less. Know your dose. Start small and wait. A full stomach can delay the onset by hours. A dose thats easy for somebody with a tolerance might be too much or even fatal for you. Invest in a milligram scale so you can accurately weigh your dosages. Bear in mind that milligram scales under ${'$'}1000 cannot accurately weigh out doses below at least 50 milligrams and are highly inaccurate under 10 - 15 milligrams. If the amounts of the drug are smaller, use volumetric dosing which means dissolving in water or alcohol to make it easier to measure. Many substances do not have linear dose-response curves, meaning that doubling the dose amount will cause a greater than double increase (and rapidly result in overwhelming, unpleasant, and potentially dangerous experiences), therefore doses should always be adjusted upward with slight increases (e.g. 1/4 to 1/2 of the previous dose)")
            Text(text = "4. Set and Setting", style = titleStyle)
            Text(text = "Set: Make sure your thoughts, desires, feelings, general mood, and any preconceived notions or expectations about what you are about to experience are conducive to the experience. Your body is also an important part of the Set. Better not to take it if you feel sick, injured or generally unhealthy.\u2028Setting: An unfamiliar, uncontrollable or otherwise disagreeable social or physical environment may result in an unpleasant or dangerous experience. Choose an environment that provides a sense of safety, familiarity, control, and comfort. For using hallucinogens (psychedelics, dissociatives and deliriants) refer to the safer Hallucinogen guide.")
            Text(text = "5. Combinations", style = titleStyle)
            Text(text = "Don’t combine drugs, including Alcohol, without research on the combo. The most common cause of substance-related deaths is the combination of depressants (such as opiates, benzodiazepines, or alcohol) with other depressants.")
            Text(text = "6. Administration Routes", style = titleStyle)
            Text(text = "Don’t share snorting equipment (straws, banknotes, bullets) to avoid blood-borne diseases such as Hepatitis C that can be transmitted through blood amounts so small you can’t notice(Safer sniffing guide link: https://ourhealthyeg.ca/safer-snorting). Injection is the the most dangerous route of administration and highly advised against. If you are determined to inject, don’t share injection materials and refer to the safer injection guide. Link the videos in this playlist: https://youtube.com/playlist?list=PLkC348-BeCu6Ut-iJy8xp9_LLKXoMMroR")
            Text(text = "7. Allergy Tests", style = titleStyle)
            Text(text = "Simply dose a minuscule amount of the substance (e.g. 1/10 to 1/4 of a regular dose) and wait several hours to verify that you do not exhibit an unusual or idiosyncratic response.")
            Text(text = "8. Reflection", style = titleStyle)
            Text(text = "Carefully monitor the frequency and intensity of any substance use to ensure it is not sliding into abuse and addiction. In particular, many stimulants, opioids, and depressants are known to be highly addictive.")
            Text(text = "9. Safety of Others", style = titleStyle)
            Text(text = "Don’t drive, operate heavy machinery, or otherwise be directly or indirectly responsible for the safety or care of another person while intoxicated.")
            Text(text = "10. Recovery Position", style = titleStyle)
            Text(text = "If someone is unconscious and breathing place them into Recovery Position to prevent death by the suffocation of vomit after a drug overdose. (youtube link: https://www.youtube.com/watch?v=dv3agW-DZ5I). Have the contact details of help services to hand in case of urgent need. ")
        }
    }
}