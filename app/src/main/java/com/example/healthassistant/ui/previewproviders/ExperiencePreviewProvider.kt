package com.example.healthassistant.ui.previewproviders

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Sentiment

class ExperiencePreviewProvider : PreviewParameterProvider<Experience> {
    override val values: Sequence<Experience> = sequenceOf(
        Experience(title = "Day at Lake Geneva", text = "Some notes", sentiment = Sentiment.SATISFIED)
    )
}