package com.example.healthassistant.ui.home.experience.addingestion

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider

@Preview
@Composable
fun ChooseRouteScreen(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Route") }) }
    ) {
        val spacing = 6
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.dp)
        ) {
            Column(
                Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(spacing.dp)
            ) {
                val otherRoutes = AdministrationRoute.values().filter { route ->
                    !substance.roas.map { it.route }.contains(route)
                }
                val otherRoutesChunked = otherRoutes.chunked(2)
                otherRoutesChunked.forEach { otherRouteChunk ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(spacing.dp)
                    ) {
                        otherRouteChunk.forEach { route ->
                            Card(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                RouteBox(route = route)
                            }
                        }
                    }
                }
            }
            Column(
                Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(spacing.dp)
            ) {
                substance.roas.forEach { roa ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f)
                    ) {
                        RouteBox(route = roa.route)
                    }
                }
            }
        }
    }
}

@Composable
fun RouteBox(route: AdministrationRoute) {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = route.displayText, style = MaterialTheme.typography.h5)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = route.description,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }
}
