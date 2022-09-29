package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SectionTitlePreview() {
    SectionTitle(title = "2022")
}

@Composable
fun SectionTitle(title: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.surface
    ) {
        Text(
            color = MaterialTheme.colors.onSurface,
            text = title,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}