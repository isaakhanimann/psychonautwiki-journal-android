package com.example.healthassistant.ui.settings.faq

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun FAQScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "FAQ") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 10.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            ExpandableContainer(
                title = "When does the app detect interactions?",
            ) {
                Text(text = "If there is an interaction with a substance that was taken less than 2 days ago.")
            }
            Divider()
            ExpandableContainer(
                title = "Why are there sometimes more interactions in the app than in the PsychonautWiki article?",
            ) {
                Text(text = "Because in the PsychonautWiki articles sometimes a substance A lists an interaction with another substance B, but substance B does not list substance A in its interactions. The app consolidates the interactions to always be mutual.")
            }
            Divider()
            ExpandableContainer(
                title = "Why is an interaction or other info on a substance present in the PsychonautWiki article but not in the app?",
            ) {
                Text(text = "There can be a couple reasons. Either the info is not annotated correctly in the article, the PsychonautWiki API does not parse the info correctly or the app does not import the info from the API correctly. In any case please report the bug.")
            }
            Divider()
            ExpandableContainer(
                title = "How can one change or add info on a substance (duration, dose, interactions and effects)?",
            ) {
                Text(text = "By editing the corresponding PsychonautWiki article.")
            }
            Divider()
            ExpandableContainer(
                title = "How is the timeline drawn?",
            ) {
                Text(
                    text = "The onset duration range from PsychonautWiki defines when the curve starts going up, the comeup how long it goes up for, the peak how long it stays up and the offset how long it takes to come down to baseline. The peak and offset durations are linearly interpolated based on the dose if possible, else it just chooses the middle value of the range.\nIf not all of these durations are defined but the total duration is given then a dotted bow is drawn.\nElse the timeline for this substance cannot be drawn.",
                )
            }
            Divider()
            ExpandableContainer(
                title = "Why does the timeline not add up the curves for the same substance?",
            ) {
                Text(text = "One can not add two curves together because one ingestion might build up tolerance, influencing the curve of the other ingestion. The curve can only be drawn based on data that is available through PsychonautWiki.")
            }
            Divider()
            ExpandableContainer(
                title = "Why does the timeline not show cumulative timelines of different substances?",
            ) {
                Text(text = "Because the curve can only be drawn based on data that is available through PsychonautWiki.")
            }
            Divider()
        }
    }
}


@Composable
fun ExpandableContainer(
    title: String,
    content: @Composable () -> Unit,
) {
    var isContentVisible by rememberSaveable {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isContentVisible = !isContentVisible
            }
            .padding(vertical = 5.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = title, style = MaterialTheme.typography.subtitle1)
        AnimatedVisibility(visible = isContentVisible) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                content()
            }
        }
    }

}