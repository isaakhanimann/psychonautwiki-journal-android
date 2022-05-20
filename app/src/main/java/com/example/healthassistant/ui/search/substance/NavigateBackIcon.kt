package com.example.healthassistant.ui.search.substance

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun NavigateBackIcon(
    navigateBack: () -> Unit,
) {
    IconButton(
        onClick = navigateBack
    ) {
        Icon(Icons.Default.ArrowBack, "backIcon")
    }
}