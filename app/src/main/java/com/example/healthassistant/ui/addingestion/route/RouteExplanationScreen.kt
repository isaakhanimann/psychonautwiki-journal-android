package com.example.healthassistant.ui.addingestion.route

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.ui.search.substance.ArticleLink

@Preview
@Composable
fun RouteExplanationScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Routes of Administration")
                },
                actions = {
                    ArticleLink(url = AdministrationRoute.psychonautWikiArticleURL)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = """A route of administration is the method in which a psychoactive substance is delivered into the body.
The route through which a substance is administered can greatly impact its potency, duration, and subjective effects. For example, many substances are more effective when consumed using particular routes of administration, while some substances are completely inactive with certain routes.
Determining an optimal route of administration is highly dependent on the substance consumed, its desired duration and potency and side effects, and one's personal comfort level."""
            )
            Spacer(modifier = Modifier.height(5.dp))
            AdministrationRoute.values().forEach {
                Text(text = it.displayText, style = MaterialTheme.typography.h6)
                Text(text = it.articleText)
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}