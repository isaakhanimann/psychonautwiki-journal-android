package com.example.healthassistant.ui.experiences

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
        color = MaterialTheme.colors.primary.copy(alpha = 0.2f)
    ) {
        Text(
            color = MaterialTheme.colors.primary,
            text = title,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}