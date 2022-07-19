package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
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
        navigateToSaferSniffingScreen = {},
        roa = roa,
        maxDurationInSeconds = null
    )
}

@Composable
fun RoaView(
    navigateToDosageExplanationScreen: () -> Unit,
    navigateToDurationExplanationScreen: () -> Unit,
    navigateToSaferSniffingScreen: () -> Unit,
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
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = roa.route.displayText, style = MaterialTheme.typography.h6)
                if (roa.route == AdministrationRoute.INSUFFLATED) {
                    Text(
                        "Safer Sniffing",
                        color = MaterialTheme.colors.primary,
                        style = MaterialTheme.typography.button,
                        modifier = Modifier
                            .clickable(onClick = navigateToSaferSniffingScreen)
                    )
                } else if (roa.route == AdministrationRoute.RECTAL) {
                    val uriHandler = LocalUriHandler.current
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { uriHandler.openUri(AdministrationRoute.saferPluggingArticleURL) }) {
                        Icon(
                            Icons.Filled.Launch,
                            contentDescription = "Open Link",
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colors.primary
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            "Safer Plugging",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.button
                        )
                    }
                } else if (roa.route.isInjectionMethod) {
                    val uriHandler = LocalUriHandler.current
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { uriHandler.openUri(AdministrationRoute.saferInjectionArticleURL) }) {
                        Icon(
                            Icons.Filled.Launch,
                            contentDescription = "Open Link",
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colors.primary
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            "Safer Injection",
                            color = MaterialTheme.colors.primary,
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
            if (!isEveryDoseNull && roaDose != null) {
                Text(text = "Dosage", style = MaterialTheme.typography.subtitle2)
                RoaDoseView(
                    roaDose = roaDose,
                    navigateToDosageExplanationScreen = navigateToDosageExplanationScreen
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            if (!isEveryDurationNull && roaDuration != null) {
                Text(text = "Duration", style = MaterialTheme.typography.subtitle2)
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