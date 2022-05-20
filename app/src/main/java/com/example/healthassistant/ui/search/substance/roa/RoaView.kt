package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Roa
import com.example.healthassistant.ui.previewproviders.RoaPreviewProvider
import com.example.healthassistant.ui.search.substance.SubstanceInfoCard

@Preview
@Composable
fun RoaView(@PreviewParameter(RoaPreviewProvider::class) roa: Roa) {
    SubstanceInfoCard(title = roa.route.displayText, isContentFaded = false) {
        Column {
            roa.roaDose?.let {
                RoaDoseView(roaDose = it)
                Spacer(modifier = Modifier.height(5.dp))
            }
            roa.roaDuration?.let {
                DurationView(roaDuration = it)
            }
        }
    }
}