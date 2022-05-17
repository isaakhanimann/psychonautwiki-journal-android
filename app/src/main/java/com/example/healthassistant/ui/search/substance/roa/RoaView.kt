package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Roa
import com.example.healthassistant.data.substances.RoaDuration
import com.example.healthassistant.ui.previewproviders.RoaPreviewProvider
import com.example.healthassistant.ui.search.substance.SubstanceInfoCard

@Preview
@Composable
fun RoaView(@PreviewParameter(RoaPreviewProvider::class) roa: Roa) {
    SubstanceInfoCard(title = roa.route.displayText) {
        Column {
            roa.roaDose?.let {
                RoaDoseView(roaDose = it)
                Divider()
            }
            roa.roaDuration?.let {
                DurationView(roaDuration = it)
            }
        }
    }
}

@Composable
fun DurationView(roaDuration: RoaDuration) {
    Column {
        Text("total: ${roaDuration.total?.text}")
        Text("after effects: ${roaDuration.afterglow?.text}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column {
                LineLow()
                Text(roaDuration.onset?.text ?: "")
            }
            Column {
                LineUp()
                Text(roaDuration.comeup?.text ?: "")
            }
            Column {
                LineHigh()
                Text(roaDuration.peak?.text ?: "")
            }
            Column {
                LineDown()
                Text(roaDuration.offset?.text ?: "")
            }
        }
    }
}

@Composable
fun LineLow() {
    Text(text = "onset")
}

@Composable
fun LineUp() {
    Text(text = "comeup")
}

@Composable
fun LineHigh() {
    Text(text = "peak")
}

@Composable
fun LineDown() {
    Text(text = "offset")
}