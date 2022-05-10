package com.example.healthassistant.presentation.search.substance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Substance
import com.google.accompanist.flowlayout.FlowRow

@Composable
fun InteractionsView(@PreviewParameter(SampleSubstanceProvider::class) substance: Substance) {
    if (substance.dangerousInteractions.isNotEmpty() || substance.unsafeInteractions.isNotEmpty() || substance.uncertainInteractions.isNotEmpty()) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column {
                Text(text = "Interactions", style = MaterialTheme.typography.h5)
                FlowRow {
                    substance.dangerousInteractions.forEach {
                        Chip(text = it, color = Color.Red)
                    }
                    substance.unsafeInteractions.forEach {
                        Chip(text = it, color = Color(0xFFFF9800))
                    }
                    substance.uncertainInteractions.forEach {
                        Chip(text = it, color = Color.Yellow)
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(text: String, color: Color) {
    Surface(
        modifier = Modifier.padding(2.dp),
        shape = RoundedCornerShape(20.dp),
        color = color
    ) {
        Text(text = text, Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
    }
}