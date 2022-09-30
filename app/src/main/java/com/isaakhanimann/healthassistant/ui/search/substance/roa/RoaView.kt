package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.data.substances.classes.roa.Roa
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.VerticalSpace
import com.isaakhanimann.healthassistant.ui.search.substance.roa.dose.RoaDoseView
import com.isaakhanimann.healthassistant.ui.search.substance.roa.duration.RoaDurationView
import com.isaakhanimann.healthassistant.ui.search.substance.roa.duration.RoaPreviewProvider
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding


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
            SectionTitle(title = roa.route.displayText)
            VerticalSpace()
            if (roa.route == AdministrationRoute.INSUFFLATED) {
                Text(
                    "Safer Sniffing",
                    color = MaterialTheme.colors.primary,
                    style = MaterialTheme.typography.button,
                    modifier = Modifier
                        .clickable(onClick = navigateToSaferSniffingScreen)
                        .padding(horizontal = horizontalPadding)
                )
            } else if (roa.route == AdministrationRoute.RECTAL) {
                val uriHandler = LocalUriHandler.current
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { uriHandler.openUri(AdministrationRoute.saferPluggingArticleURL) }
                        .padding(horizontal = horizontalPadding)) {
                    Icon(
                        Icons.Default.OpenInBrowser,
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
                        .clickable { uriHandler.openUri(AdministrationRoute.saferInjectionArticleURL) }
                        .padding(horizontal = horizontalPadding)) {
                    Icon(
                        Icons.Default.OpenInBrowser,
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
            if (!isEveryDoseNull && roaDose != null) {
                Text(text = "Dosage", modifier = Modifier.padding(horizontal = horizontalPadding))
                RoaDoseView(
                    roaDose = roaDose,
                    navigateToDosageExplanationScreen = navigateToDosageExplanationScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )
                VerticalSpace()
            }
            if (!isEveryDurationNull && roaDuration != null) {
                Text(text = "Duration", modifier = Modifier.padding(horizontal = horizontalPadding))
                RoaDurationView(
                    roaDuration = roaDuration,
                    maxDurationInSeconds = maxDurationInSeconds,
                    isOralRoute = roa.route == AdministrationRoute.ORAL,
                    navigateToDurationExplanationScreen = navigateToDurationExplanationScreen,
                    modifier = Modifier.padding(horizontal = horizontalPadding)
                )
            }
        }
    }
}