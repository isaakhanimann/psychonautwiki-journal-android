/*
 * Copyright (c) 2022. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.isaakhanimann.journal.ui.main.MainScreen
import com.isaakhanimann.journal.ui.tabs.safer.settings.DOWNLOAD_URL
import com.isaakhanimann.journal.ui.theme.JournalTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: CheckVersionViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
        setContent {
            JournalTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !isSystemInDarkTheme()
                val systemBarColors = NavigationBarDefaults.containerColor
                DisposableEffect(systemUiController, useDarkIcons) {
                    systemUiController.setSystemBarsColor(
                        color = systemBarColors,
                        darkIcons = useDarkIcons
                    )
                    onDispose {}
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
                val uriHandler = LocalUriHandler.current
                AnimatedVisibility(visible = viewModel.isShowingNewerVersionAlert.value) {
                    AlertDialog(
                        onDismissRequest = {
                            viewModel.isShowingNewerVersionAlert.value = false
                        },
                        title = {
                            Text(text = "Newer Version Available")
                        },
                        text = {
                            Text(viewModel.newerVersionText.value)
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    viewModel.isShowingNewerVersionAlert.value = false
                                    uriHandler.openUri(DOWNLOAD_URL)
                                }
                            ) {
                                Text("Visit Website")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    viewModel.isShowingNewerVersionAlert.value = false
                                }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkVersion()
    }
}
