package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Mood

class ExperiencesPreviewProvider : PreviewParameterProvider<List<Experience>> {
    override val values: Sequence<List<Experience>> = sequenceOf(
        listOf(
            Experience(
                id = 21,
                title = "Day at Lake Geneva",
                text = "Some notes",
                mood = Mood.SATISFIED
            ),
            Experience(
                id = 22,
                title = "This one has a very very very long title in case somebody wants to be creative with the naming.",
                text = "Some notes",
                mood = Mood.VERY_DISSATISFIED
            )
        ) + List(20) {
            Experience(
                id = it,
                title = "Experience $it",
                text = "Some notes",
                mood = Mood.NEUTRAL
            )
        },
        listOf()
    )
}