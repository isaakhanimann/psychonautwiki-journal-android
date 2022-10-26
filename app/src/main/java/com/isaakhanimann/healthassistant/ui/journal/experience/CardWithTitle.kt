package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CardWithTitle(
    title: String,
    innerPaddingVertical: Dp = 5.dp,
    innerPaddingHorizontal: Dp = 10.dp,
    content: @Composable () -> Unit
) {
    Card(modifier = Modifier.padding(vertical = 5.dp)) {
        Column(
            Modifier.padding(
                horizontal = innerPaddingHorizontal,
                vertical = innerPaddingVertical
            )
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            content()
        }
    }
}