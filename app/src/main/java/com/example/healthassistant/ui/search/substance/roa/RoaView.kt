package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Dosage", style = MaterialTheme.typography.subtitle1)
                    IconButton(onClick = navigateToDosageExplanationScreen) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = "Dosage Info",
                            Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                }
                RoaDoseView(roaDose = roaDose)
                Spacer(modifier = Modifier.height(5.dp))
            }
            if (!isEveryDurationNull && roaDuration != null) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Duration", style = MaterialTheme.typography.subtitle1)
                    IconButton(onClick = navigateToDurationExplanationScreen) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = "Dosage Info",
                            Modifier.size(ButtonDefaults.IconSize)
                        )
                    }
                }
                RoaDurationView(
                    roaDuration = roaDuration,
                    maxDurationInSeconds = maxDurationInSeconds,
                    isOralRoute = roa.route == AdministrationRoute.ORAL
                )
            }
        }
    }
}