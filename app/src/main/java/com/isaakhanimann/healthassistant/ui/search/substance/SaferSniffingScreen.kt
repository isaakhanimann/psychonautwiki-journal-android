package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun SaferSniffingScreen() {
    Scaffold(
        topBar = {
            JournalTopAppBar(title = "Safer Sniffing")
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            SectionText(
                text = """
                The nose is a sensitive organ: fine hairs protect the nasal mucous membranes from external impurities (dust, pollen, etc.). If a foreign substance is now ingested through the nose, this and its mucous membranes are subjected to above-average stress. This can lead to disturbances in smelling or even to holes in the nasal mucous membranes. If sniffing utensils are shared, there is also a risk of bacteria and viruses being transmitted. If the utensils are contaminated with blood, the worst case scenario is the transmission of hepatitis C viruses. If the nasal mucous membranes are irritated and injured, the probability of such transmission is all the higher. Therefore, when you have a cold, it is important to give the sensitive organ of the nose the care it needs.
            """.trimIndent()
            )
            Spacer(modifier = Modifier.height(5.dp))
            SectionTitle(title = "Hygiene First")
            SectionText(
                text = """
                Pay attention to hygiene in general, wash your hands before and after consumption and always use a clean, dry and smooth surface (mirror or similar). Do not snort on/from dirty surfaces such as toilets.
            """.trimIndent()
            )
            SectionTitle(title = "Mine is Mine")
            SectionText(
                text = """
                Always use your personal sniffing utensil (sniffing tube or sniffing utensil). Do not share utensils as this is unhygienic and can transmit diseases. Banknotes are not suitable as they are often carriers of bacteria and viruses (they pass through many hands) and are sometimes printed with inks containing heavy metals. Hard plastic tubes carry the risk of a nose injury (sharp edge), the same applies to thin paper. Non-printed, slightly thicker paper (100 mg) with a smooth surface is best.
            """.trimIndent()
            )
            SectionTitle(title = "Powder is Not Just Powder")
            SectionText(
                text = """
                Crush the powder as finely as possible and dose low! The active ingredient content can vary greatly from time to time, even if the substance comes from the same source. The quality of a powder cannot be determined visually, by taste or smell. Even a gum test for cocaine says nothing about the quality of the powder.
            """.trimIndent()
            )
            SectionTitle(title = "Take Care of Your Nose")
            SectionText(
                text = """
                Blow your nose before sniffling and for about 10 minutes afterwards! The nasal septum, nasal mucous membranes and olfactory cells can be severely irritated and dried out by sniffing and this can lead to inflammation, boils and even dead tissue on the nasal septum. That is why you should rinse your nose after consumption or at the latest the next day and care for it with a moisturising/fatty cream.
            """.trimIndent()
            )
            SectionTitle(title = "Nasal Rinsing")
            SectionText(
                text = """
                To do this, you need 2.5 grams of nasal rinsing salt (available in pharmacies; pure sea salt is also sufficient. 2.5 grams correspond to about half a teaspoon - if you are unsure, it is better to use a little more than less. It should not burn!) and dissolve it in 2.5 decilitres of lukewarm water. You can also buy ready-made sea salt nasal sprays at the pharmacy/drugstore - do not use nasal sprays for colds, as these lead to additional drying of the nasal mucous membranes. You can either buy a nasal douche or run the saline solution from a cup or your hand into your nose, pull and let it run out again. It is best to "flow" into one nostril and "drain" out through the other.
            """.trimIndent()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun SectionText(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Justify,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = horizontalPadding)
    )
}