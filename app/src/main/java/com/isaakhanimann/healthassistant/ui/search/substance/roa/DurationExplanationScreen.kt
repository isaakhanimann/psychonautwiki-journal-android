package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.SectionText
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun DurationExplanationScreen() {
    Scaffold(
        topBar = {
            JournalTopAppBar(title = "Duration Classifications")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
        ) {
            SectionText(
                text = "Duration refers to the length of time over which the subjective effects of a psychoactive substance manifest themselves.\n" +
                        "Duration can be broken down into 6 parts: (1) total duration (2) onset (3) come up (4) peak (5) offset and (6) after effects. Depending upon the substance consumed, each of these occurs in a separate and continuous fashion."
            )
            SectionTitle(title = "Total")
            SectionText(text = "The total duration of a substance can be defined as the amount of time it takes for the effects of a substance to completely wear off into sobriety, starting from the moment the substance is first administered.")
            SectionTitle(title = "Onset")
            SectionText(text = "The onset phase can be defined as the period until the very first changes in perception (i.e. \"first alerts\") are able to be detected.")
            SectionTitle(title = "Come up")
            SectionText(text = "The \"come up\" phase can be defined as the period between the first noticeable changes in perception and the point of highest subjective intensity. This is colloquially known as \"coming up.\"")
            SectionTitle(title = "Peak")
            SectionText(text = "The peak phase can be defined as period of time in which the intensity of the substance's effects are at its height.")
            SectionTitle(title = "Offset")
            SectionText(text = "The offset phase can be defined as the amount of time in between the conclusion of the peak and shifting into a sober state. This is colloquially referred to as \"coming down.\"")
            SectionTitle(title = "After Effects")
            SectionText(text = "The after effects can be defined as any residual effects which may remain after the experience has reached its conclusion. This is colloquially known as a \"hangover\" or an \"afterglow\" depending on the substance and usage.\n" +
                    "The after effects are not included as part of the total duration.")
        }
    }
}