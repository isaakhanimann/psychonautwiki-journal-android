package com.example.healthassistant.presentation.search.substance.roa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Roa
import com.example.healthassistant.data.substances.RoaDose

@Preview
@Composable
fun RoaView(@PreviewParameter(SampleRoaProvider::class) roa: Roa) {
    Card(modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = roa.name, style = MaterialTheme.typography.h5)
            roa.roaDose?.let {
                DoseView(it)
            }

        }
    }
}

@Composable
fun DoseView(roaDose: RoaDose) {
    Text(text = "Dose", style = MaterialTheme.typography.h6)
    Text(text = "threshold: ${roaDose.threshold}")
}