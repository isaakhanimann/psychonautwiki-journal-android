package com.example.healthassistant.ui.addingestion.route

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.substances.AdministrationRoute

@Composable
fun ChooseRouteScreen(
    navigateToChooseDose: (administrationRoute: AdministrationRoute) -> Unit,
    navigateToRouteExplanationScreen: () -> Unit,
    viewModel: ChooseRouteViewModel = hiltViewModel()
) {
    ChooseRouteScreen(
        shouldShowOther = viewModel.shouldShowOtherRoutes,
        onChangeShowOther = {
            viewModel.shouldShowOtherRoutes = it
        },
        pwRoutes = viewModel.pwRoutes,
        otherRoutesChunked = viewModel.otherRoutesChunked,
        onRouteTapped = { route ->
            if (route.isInjectionMethod) {
                viewModel.currentRoute = route
                viewModel.isShowingInjectionDialog = true
            } else {
                navigateToChooseDose(route)
            }
        },
        navigateToRouteExplanationScreen = navigateToRouteExplanationScreen,
        isShowingInjectionDialog = viewModel.isShowingInjectionDialog,
        navigateWithCurrentRoute = {
            navigateToChooseDose(viewModel.currentRoute)
        },
        dismissInjectionDialog = {
            viewModel.isShowingInjectionDialog = false
        }
    )
}

@Preview
@Composable
fun ChooseRouteScreenPreview() {
    val pwRoutes = listOf(AdministrationRoute.INSUFFLATED, AdministrationRoute.ORAL)
    val otherRoutes = AdministrationRoute.values().filter { route ->
        !pwRoutes.contains(route)
    }
    val otherRoutesChunked = otherRoutes.chunked(2)
    ChooseRouteScreen(
        shouldShowOther = true,
        onChangeShowOther = {},
        pwRoutes = pwRoutes,
        otherRoutesChunked = otherRoutesChunked,
        onRouteTapped = {},
        navigateToRouteExplanationScreen = {},
        isShowingInjectionDialog = false,
        navigateWithCurrentRoute = {},
        dismissInjectionDialog = {}
    )
}

@Composable
fun ChooseRouteScreen(
    shouldShowOther: Boolean,
    onChangeShowOther: (Boolean) -> Unit,
    pwRoutes: List<AdministrationRoute>,
    otherRoutesChunked: List<List<AdministrationRoute>>,
    onRouteTapped: (route: AdministrationRoute) -> Unit,
    navigateToRouteExplanationScreen: () -> Unit,
    isShowingInjectionDialog: Boolean,
    navigateWithCurrentRoute: () -> Unit,
    dismissInjectionDialog: () -> Unit
) {
    Column {
        LinearProgressIndicator(progress = 0.5f, modifier = Modifier.fillMaxWidth())
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Choose Route") },
                    navigationIcon = if (shouldShowOther && pwRoutes.isNotEmpty()) {
                        {
                            IconButton(onClick = { onChangeShowOther(false) }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    } else null,
                    actions = {
                        IconButton(onClick = navigateToRouteExplanationScreen) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Administration Routes Info"
                            )
                        }
                    }
                )
            }
        ) {
            val spacing = 6
            if (isShowingInjectionDialog) {
                InjectionDialog(
                    navigateToNext = navigateWithCurrentRoute,
                    dismiss = dismissInjectionDialog
                )
            }
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
                                                onRouteTapped(route)
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
                        pwRoutes.forEach { route ->
                            Card(
                                modifier = Modifier
                                    .clickable {
                                        onRouteTapped(route)
                                    }
                                    .fillMaxWidth()
                                    .weight(5f)
                            ) {
                                RouteBox(route = route, titleStyle = MaterialTheme.typography.h4)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun InjectionDialogPreview() {
    InjectionDialog(
        navigateToNext = {},
        dismiss = {}
    )
}

@Composable
fun InjectionDialog(
    navigateToNext: () -> Unit,
    dismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning")
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(text = "Safer Injection", style = MaterialTheme.typography.h5)
            }
        },
        text = {
            Column {
                Text("Using and sharing injection equipment is an extremely high-risk activity and is never truly safe to do in a non-medical context.")
                Text("Read the safer injection guide:")
                SaferInjectionLink()
                Text("This guide is provided for educational and harm reduction purposes only and we strongly discourage users from engaging in this activity.")

            }

        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    navigateToNext()
                }
            ) {
                Text("Continue")
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun RouteBox(route: AdministrationRoute, titleStyle: TextStyle) {
    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = route.displayText,
                textAlign = TextAlign.Center,
                style = titleStyle
            )
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
