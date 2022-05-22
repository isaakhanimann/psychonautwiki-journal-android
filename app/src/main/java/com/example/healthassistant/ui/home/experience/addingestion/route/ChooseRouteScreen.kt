package com.example.healthassistant.ui.home.experience.addingestion.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.AdministrationRoute
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.ui.previewproviders.SubstancePreviewProvider

@Composable
fun ChooseRouteScreen(
    navigateToChooseDose: (administrationRoute: AdministrationRoute) -> Unit,
    viewModel: ChooseRouteViewModel = hiltViewModel()
) {
    viewModel.substance?.also { sub ->
        ChooseRouteScreenContent(
            substance = sub,
            navigateToNext = { route ->
                navigateToChooseDose(route)
            }
        )
    }
}

@Preview
@Composable
fun ChooseRouteScreenContent(
    @PreviewParameter(SubstancePreviewProvider::class) substance: Substance,
    navigateToNext: (AdministrationRoute) -> Unit = {}
) {
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
                                    .clickable {
                                        navigateToNext(route)
                                    }
                                    .fillMaxHeight()
                                    .weight(1f)
                            ) {
                                RouteBox(route = route, titleStyle = MaterialTheme.typography.h6)
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
                            .clickable {
                                navigateToNext(roa.route)
                            }
                            .fillMaxWidth()
                            .weight(5f)
                    ) {
                        RouteBox(route = roa.route, titleStyle = MaterialTheme.typography.h4)
                    }
                }
            }
        }
    }
}

@Composable
fun RouteBox(route: AdministrationRoute, titleStyle: TextStyle) {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = route.displayText, style = titleStyle)
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
