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
import com.example.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.example.healthassistant.ui.search.substance.roa.duration.RoaDurationView
import com.example.healthassistant.ui.search.substance.roa.duration.RoaPreviewProvider


@Preview
@Composable
fun RoaPreview(
    @PreviewParameter(RoaPreviewProvider::class) roa: Roa,
) {
    RoaView(
        navigateToDosageExplanationScreen = {},
        navigateToDurationExplanationScreen = {},
        roa = roa,
        maxDurationInSeconds = null
    )
}

@Composable
fun RoaView(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    roa: Roa,
    maxDurationInSeconds: Float?,
) {
    val roaDose = roa.roaDose
    val isEveryDoseNull =
        roaDose?.threshold == null && roaDose?.light == null && roaDose?.common == null && roaDose?.strong == null && roaDose?.heavy == null
    val roaDuration = roa.roaDuration
    val isEveryDurationNull =
        roaDuration?.total == null && roaDuration?.onset == null && roaDuration?.comeup == null && roaDuration?.peak == null && roaDuration?.offset == null
    val isSomethingDefined = !(isEveryDoseNull && isEveryDurationNull)
    if (isSomethingDefined) {
        Column {
            Text(text = roa.route.displayText, style = MaterialTheme.typography.h6)
            if (!isEveryDoseNull && roaDose != null) {
                Text(text = "Dosage", style = MaterialTheme.typography.subtitle1)
                RoaDoseView(
                    roaDose = roaDose,
                    navigateToDosageExplanationScreen = navigateToDosageExplanationScreen
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            if (!isEveryDurationNull && roaDuration != null) {
                Text(text = "Duration", style = MaterialTheme.typography.subtitle1)
                RoaDurationView(
                    roaDuration = roaDuration,
                    maxDurationInSeconds = maxDurationInSeconds,
                    isOralRoute = roa.route == AdministrationRoute.ORAL,
                    navigateToDurationExplanationScreen = navigateToDurationExplanationScreen
                )
            }
        }
    }
}