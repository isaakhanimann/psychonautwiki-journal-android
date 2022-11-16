/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.substance

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UrlScreen(url: String) {
    val context = LocalContext.current
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                context.startActivity(shareIntent)
            }) {
                Icon(Icons.Filled.Share, "Share Link")
            }
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val state = rememberWebViewState(url = url)
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress, modifier = Modifier.fillMaxWidth()
                )
            }
            WebView(
                state = state,
                modifier = Modifier.weight(1f),
            )
        }
    }
}