package com.example.healthassistant.ui.journal.experience.addingestion.route

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
    ChooseRouteScreen(
        shouldShowOther = viewModel.shouldShowOtherRoutes,
        onChangeShowOther = {
            viewModel.shouldShowOtherRoutes = it
        },
        pwRoutes = viewModel.pwRoutes,
        otherRoutesChunked = viewModel.otherRoutesChunked,
        navigateToNext = { route ->
            navigateToChooseDose(route)
        }
    )
}

@Preview
@Composable
fun ChooseRouteScreenPreview(@PreviewParameter(SubstancePreviewProvider::class) substance: Substance) {
    val pwRoutes = listOf(AdministrationRoute.INSUFFLATED, AdministrationRoute.ORAL)
    val otherRoutes = AdministrationRoute.values().filter { route ->
        !pwRoutes.contains(route)
    }
    val otherRoutesChunked = otherRoutes.chunked(2)
    ChooseRouteScreen(
        shouldShowOther = false,
        onChangeShowOther = {},
        pwRoutes = pwRoutes,
        otherRoutesChunked = otherRoutesChunked,
        navigateToNext = {}
    )
}

@Composable
fun ChooseRouteScreen(
    shouldShowOther: Boolean,
    onChangeShowOther: (Boolean) -> Unit,
    pwRoutes: List<AdministrationRoute>,
    otherRoutesChunked: List<List<AdministrationRoute>>,
    navigateToNext: (AdministrationRoute) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Choose Route") },
                navigationIcon = if (shouldShowOther && pwRoutes.isNotEmpty()) {
                    {
                        IconButton(onClick = { onChangeShowOther(false) }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                } else null
            )
        }
    ) {
        val spacing = 6
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(spacing.dp)
        ) {
            val isShowingOther = shouldShowOther || pwRoutes.isEmpty()
            AnimatedVisibility(
                visible = isShowingOther,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.dp)
                ) {
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
                                    RouteBox(
                                        route = route,
                                        titleStyle = MaterialTheme.typography.h6
                                    )
                                }
                            }
                            if (otherRouteChunk.size == 1) {
                                Box(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = !isShowingOther,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(spacing.dp)
                ) {
                    pwRoutes.forEach { route ->
                        Card(
                            modifier = Modifier
                                .clickable {
                                    navigateToNext(route)
                                }
                                .fillMaxWidth()
                                .weight(5f)
                        ) {
                            RouteBox(route = route, titleStyle = MaterialTheme.typography.h4)
                        }
                    }
                    Card(
                        modifier = Modifier
                            .clickable {
                                onChangeShowOther(true)
                            }
                            .fillMaxWidth()
                            .weight(5f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = "Other Routes", style = MaterialTheme.typography.h4)
                        }
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
