/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.search.substance

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
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
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            Toast.makeText(
                context,
                "Link Copied",
                Toast.LENGTH_SHORT
            ).show()
        }) {
            Icon(Icons.Filled.ContentCopy, "Localized description")
        }
    }) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val state = rememberWebViewState(url = url)
            val loadingState = state.loadingState
            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    progress = loadingState.progress,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            WebView(
                state = state,
                modifier = Modifier.weight(1f),
            )
        }
    }
}