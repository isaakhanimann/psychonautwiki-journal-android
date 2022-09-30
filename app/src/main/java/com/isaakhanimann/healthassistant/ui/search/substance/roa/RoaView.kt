package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
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
            val uriHandler = LocalUriHandler.current
            if (roa.route == AdministrationRoute.INSUFFLATED) {
                TextButton(
                    onClick = navigateToSaferSniffingScreen,
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Text("Safer Sniffing")
                }
            } else if (roa.route == AdministrationRoute.RECTAL) {
                TextButton(
                    onClick = { uriHandler.openUri(AdministrationRoute.saferPluggingArticleURL) },
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link",
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Safer Plugging")
                }
            } else if (roa.route.isInjectionMethod) {
                TextButton(
                    onClick = { uriHandler.openUri(AdministrationRoute.saferInjectionArticleURL) },
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link",
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Safer Injection")
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