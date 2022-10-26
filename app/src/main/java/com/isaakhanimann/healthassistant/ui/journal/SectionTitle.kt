package com.isaakhanimann.healthassistant.ui.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding

@Preview
@Composable
fun SectionTitlePreview() {
    SectionTitle(title = "2022", onInfoClick = {})
}

@Composable
fun SectionTitle(title: String, onInfoClick: (() -> Unit)? = null) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = 3.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                text = title,
            )
            if (onInfoClick != null) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Info",
                    modifier = Modifier.clickable(onClick = onInfoClick).size(20.dp)
                )
            }

        }
    }
}