package com.example.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Substance
import com.google.accompanist.flowlayout.FlowRow

@Preview
@Composable
fun InteractionsView(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    if (substance.dangerousInteractions.isNotEmpty() || substance.unsafeInteractions.isNotEmpty() || substance.uncertainInteractions.isNotEmpty()) {
        SubstanceInfoCard(title = "Interactions") {
            FlowRow {
                substance.dangerousInteractions.forEach {
                    InteractionChip(text = it, color = Color.Red)
                }
                substance.unsafeInteractions.forEach {
                    InteractionChip(text = it, color = Color(0xFFFF9800))
                }
                substance.uncertainInteractions.forEach {
                    InteractionChip(text = it, color = Color.Yellow)
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