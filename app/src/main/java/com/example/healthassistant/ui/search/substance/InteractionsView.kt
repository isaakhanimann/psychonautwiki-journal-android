package com.example.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider
import com.google.accompanist.flowlayout.FlowRow

@Preview
@Composable
fun InteractionsPreview(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    InteractionsView(
        isSearchingForInteractions = true,
        dangerousInteractions = substance.dangerousInteractions,
        unsafeInteractions = substance.unsafeInteractions,
        uncertainInteractions = substance.uncertainInteractions
    )
}

@Composable
fun InteractionsView(
    isSearchingForInteractions: Boolean,
    dangerousInteractions: List<String>,
    unsafeInteractions: List<String>,
    uncertainInteractions: List<String>,
) {
    if (dangerousInteractions.isNotEmpty() || unsafeInteractions.isNotEmpty() || uncertainInteractions.isNotEmpty()) {
        Column {
            if (isSearchingForInteractions) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
            SubstanceInfoCard(title = "Interactions") {
                FlowRow {
                    dangerousInteractions.forEach {
                        InteractionChip(text = it, color = Color.Red)
                    }
                    unsafeInteractions.forEach {
                        InteractionChip(text = it, color = Color(0xFFFF9800))
                    }
                    uncertainInteractions.forEach {
                        InteractionChip(text = it, color = Color.Yellow)
                    }
                }
            }
        }
    }
}

@Composable
fun InteractionChip(text: String, color: Color) {
    Surface(
        modifier = Modifier.padding(2.dp),
        shape = RoundedCornerShape(20.dp),
        color = color
    ) {
        Text(text = text, modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), color = Color.Black)
    }
}