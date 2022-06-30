package com.example.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
            if (dangerousInteractions.isNotEmpty()) {
                Text(text = "Dangerous Interactions", style = MaterialTheme.typography.h6)
                FlowRow {
                    dangerousInteractions.forEach {
                        InteractionChip(text = it, color = Color.Red)
                    }
                }
            }
            if (unsafeInteractions.isNotEmpty()) {
                Text(text = "Unsafe Interactions", style = MaterialTheme.typography.h6)
                FlowRow {
                    unsafeInteractions.forEach {
                        InteractionChip(text = it, color = Color(0xFFFF9800))
                    }
                }
            }
            if (uncertainInteractions.isNotEmpty()) {
                Text(text = "Uncertain Interactions", style = MaterialTheme.typography.h6)
                FlowRow {
                    uncertainInteractions.forEach {
                        InteractionChip(text = it, color = Color.Yellow)
                    }
                }
            }
            Divider(modifier = Modifier.padding(top = 8.dp))
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