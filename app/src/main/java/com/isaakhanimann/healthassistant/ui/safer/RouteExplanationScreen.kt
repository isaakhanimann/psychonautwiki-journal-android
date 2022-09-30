package com.isaakhanimann.healthassistant.ui.safer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.AdministrationRoute
import com.isaakhanimann.healthassistant.ui.journal.SectionTitle
import com.isaakhanimann.healthassistant.ui.search.substance.SectionText
import com.isaakhanimann.healthassistant.ui.search.substance.VerticalSpace
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar

@Preview
@Composable
fun RouteExplanationScreen() {
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            JournalTopAppBar(title = "Routes of Administration")
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { uriHandler.openUri(AdministrationRoute.psychonautWikiArticleURL) },
                icon = {
                    Icon(
                        Icons.Default.OpenInBrowser,
                        contentDescription = "Open Link"
                    )
                },
                text = { Text("Article") },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SectionText(
                text = """A route of administration is the method in which a psychoactive substance is delivered into the body.
The route through which a substance is administered can greatly impact its potency, duration, and subjective effects. For example, many substances are more effective when consumed using particular routes of administration, while some substances are completely inactive with certain routes.
Determining an optimal route of administration is highly dependent on the substance consumed, its desired duration and potency and side effects, and one's personal comfort level."""
            )
            AdministrationRoute.values().filter { !it.isInjectionMethod }.forEach {
                SectionTitle(it.displayText)
                SectionText(it.articleText)
                if (it == AdministrationRoute.RECTAL) {
                    Button(
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
                    VerticalSpace()
                }
            }
            AdministrationRoute.values().filter { it.isInjectionMethod }.forEach {
                SectionTitle(it.displayText)
                SectionText(it.articleText)
            }
        }
    }
}