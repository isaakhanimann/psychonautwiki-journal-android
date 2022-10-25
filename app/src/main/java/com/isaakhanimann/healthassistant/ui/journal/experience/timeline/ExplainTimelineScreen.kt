package com.isaakhanimann.healthassistant.ui.journal.experience.timeline

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.BulletPoints
import com.isaakhanimann.healthassistant.ui.search.substance.SectionText
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun ExplainTimelineScreen() {
    Scaffold(
        topBar = {
            JournalTopAppBar(
                title = "Timeline Info",
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            BulletPoints(
                points = listOf(
                    "The vertical trajectory of dotted lines is unknown",
                    "A full stomach delays the onset of an orally consumed substance by approximately 3 hours",
                    "Heavy doses can have longer durations",
                ),
                modifier = Modifier.padding(horizontal = horizontalPadding)
            )
            Spacer(modifier = Modifier.height(10.dp))
            SectionTitle(title = "Incomplete Timelines")
            SectionText(text = "If there is no timeline or part of the timeline is missing that means that the duration is not defined in PsychonautWiki. If you add the missing durations in PsychonautWiki, the full timeline will be shown in the next update. ")
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}