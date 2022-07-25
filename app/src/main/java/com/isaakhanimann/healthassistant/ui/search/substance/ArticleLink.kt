package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler

@Composable
fun ArticleLink(url: String) {
    val uriHandler = LocalUriHandler.current
    TextButton(onClick = {
        uriHandler.openUri(url)
    }) {
        val color =
            if (isSystemInDarkTheme()) MaterialTheme.colors.primary else MaterialTheme.colors.onPrimary
        Icon(
            Icons.Filled.Launch,
            contentDescription = "Open Link",
            modifier = Modifier.size(ButtonDefaults.IconSize),
            tint = color
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            "Article", color = color
        )
    }
}
