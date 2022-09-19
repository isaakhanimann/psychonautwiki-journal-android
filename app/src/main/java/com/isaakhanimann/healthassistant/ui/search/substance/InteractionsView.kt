package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.InteractionType
import com.isaakhanimann.healthassistant.data.substances.classes.Interactions

@Preview
@Composable
fun InteractionsPreview(@PreviewParameter(InteractionsPreviewProvider::class) interactions: Interactions) {
    InteractionsView(
        interactions
    )
}

@Composable
fun InteractionsView(
    interactions: Interactions?,
) {
    if (interactions != null) {
        if (interactions.dangerous.isNotEmpty() || interactions.unsafe.isNotEmpty() || interactions.uncertain.isNotEmpty()) {
            Column {
                val titleStyle = MaterialTheme.typography.subtitle2
                Text(text = "Interactions", style = titleStyle)
                if (interactions.dangerous.isNotEmpty()) {
                    interactions.dangerous.forEach {
                        InteractionChip(text = it, color = InteractionType.DANGEROUS.color)
                    }
                }
                if (interactions.unsafe.isNotEmpty()) {
                    interactions.unsafe.forEach {
                        InteractionChip(text = it, color = InteractionType.UNSAFE.color)

                    }
                }
                if (interactions.uncertain.isNotEmpty()) {
                    interactions.uncertain.forEach {
                        InteractionChip(text = it, color = InteractionType.UNCERTAIN.color)
                    }
                }
                Text(
                    text = "Check the PsychonautWiki article for explanations",
                    style = MaterialTheme.typography.caption
                )
                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun InteractionChip(text: String, color: Color) {
    Surface(
        modifier = Modifier.padding(vertical = 0.5.dp).fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        color = color
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
    }
}