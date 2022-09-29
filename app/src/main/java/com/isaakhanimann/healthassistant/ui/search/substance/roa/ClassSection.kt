package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun ClassSectionPreview() {
    ClassSection(
        psychoactiveClasses = listOf("Stimulants", "Psychedelics"),
        chemicalClasses = listOf("Substituted Phenethylamines"),
        titleStyle = MaterialTheme.typography.h6
    )
}

@Composable
fun ClassSection(
    psychoactiveClasses: List<String>,
    chemicalClasses: List<String>,
    titleStyle: TextStyle
) {
    if (psychoactiveClasses.isNotEmpty() || chemicalClasses.isNotEmpty()) {
        Column {
            Text(text = "Class Membership", style = titleStyle)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (psychoactiveClasses.isNotEmpty()) {
                    Column {
                        Text(text = "Psychoactive")
                        psychoactiveClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
                if (chemicalClasses.isNotEmpty()) {
                    Column {
                        Text(text = "Chemical")
                        chemicalClasses.forEach {
                            Text(text = it)
                        }
                    }
                }
            }

        }
        Divider()
    }
}