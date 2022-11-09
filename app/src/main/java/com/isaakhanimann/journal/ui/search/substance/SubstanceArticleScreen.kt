package com.isaakhanimann.journal.ui.search.substance

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Preview
@Composable
fun SubstanceArticleScreen() {
    val url = "https://google.com"
    val state = rememberWebViewState(url = url)

    Column {
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