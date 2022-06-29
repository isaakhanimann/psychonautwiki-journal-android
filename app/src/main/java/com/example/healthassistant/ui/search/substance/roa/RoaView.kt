package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Roa
import com.example.healthassistant.ui.search.substance.SubstanceInfoCard
import com.example.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.example.healthassistant.ui.search.substance.roa.duration.RoaDurationView
import com.example.healthassistant.ui.search.substance.roa.duration.RoaPreviewProvider


@Preview
@Composable
fun RoaPreview(
    @PreviewParameter(RoaPreviewProvider::class) roa: Roa,
    ) {
    RoaView(roa = roa, maxDurationInSeconds = null)
}

@Composable
fun RoaView(
    roa: Roa,
    maxDurationInSeconds: Float?
) {
    SubstanceInfoCard(title = roa.route.displayText, isContentFaded = false) {
        Column {
            roa.roaDose?.also {
                Text(text = "Dosage", style = MaterialTheme.typography.subtitle1)
                RoaDoseView(roaDose = it)
                Spacer(modifier = Modifier.height(5.dp))
            }
            roa.roaDuration?.also {
                Text(text = "Duration", style = MaterialTheme.typography.subtitle1)
                RoaDurationView(roaDuration = it, maxDurationInSeconds = maxDurationInSeconds, isOralRoute = roa.route == AdministrationRoute.ORAL)
            }
        }
    }
}