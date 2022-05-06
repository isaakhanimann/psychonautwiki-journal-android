package com.example.healthassistant.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


data class Experience(val title: String, val substanceNames: List<String>)

val previewExperiences = listOf(
    Experience("14. May 2022", substanceNames = listOf("MDMA")),
    Experience("28. Apr 2022", substanceNames = listOf("Cocaine")),
    Experience("4. Apr 2022", substanceNames = listOf("LSD")),
    Experience("15. Mar 2022", substanceNames = listOf("LSD", "MDMA")),
    Experience("7. Feb 2022", substanceNames = listOf("Cannabis", "2C-B")),
    Experience("4. Feb 2022", substanceNames = listOf("Cocaine", "LSD")),
    Experience("2. Jan 2022", substanceNames = listOf("Ketamine")),
    Experience("23. Jan 2022", substanceNames = listOf("MDMA")),
    Experience("31. Dec 2021", substanceNames = listOf("MDMA")),
    Experience("27. Nov 2021", substanceNames = listOf("Cocaine")),
    Experience("11. Oct 2021", substanceNames = listOf("Amphetamine", "MDMA")),
    Experience("29. Sep 2021", substanceNames = listOf("Amphetamine")),
    Experience("25. Sep 2021", substanceNames = listOf("2C-B")),
    Experience("18. Aug 2021", substanceNames = listOf("Cannabis", "LSD")),
    Experience("14. Aug 2021", substanceNames = listOf("Cocaine")),
    Experience("24. Jul 2021", substanceNames = listOf("MDMA", "2C-B")),
    Experience("15. Jul 2021", substanceNames = listOf("Cocaine")),
    Experience("5. Jul 2021", substanceNames = listOf("MDMA", "Caffeine")),
    Experience("2. Jul 2021", substanceNames = listOf("Mescaline")),
    Experience("23. Jun 2021", substanceNames = listOf("Ketamine", "LSD", "DMT")),
    Experience("22. Jun 2021", substanceNames = listOf("Cocaine"))
)

@Preview
@Composable
fun Home(experiences: List<Experience> = previewExperiences) {
    Scaffold(
        topBar = {
            TopAppBar(title = {Text(text = "Experiences")})
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(experiences) { experience ->
                ExperienceRow(experience = experience)
            }
        }
    }
}

@Composable
fun ExperienceRow(experience: Experience) {
    Card(modifier = Modifier.fillMaxWidth(),elevation = 4.dp) {
        Row(modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp), verticalAlignment = Alignment.Bottom) {
            Text(text = experience.title, modifier = Modifier.padding(end = 10.dp), style = MaterialTheme.typography.body1)
            val altNamesString = experience.substanceNames.fold("") { acc, string -> "$acc, $string" }.removePrefix(", ")
            Text(text = altNamesString, style = MaterialTheme.typography.body2)
        }
    }
}