package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChipLink(
    name: String,
    color: Color,
    url: String?
) {
    val uriHandler = LocalUriHandler.current
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(shape = CircleShape)
            .background(color = color.copy(alpha = 0.2f))
            .clickable {
                if (url != null) {
                    uriHandler.openUri(url)
                }
            }
            .padding(vertical = 2.dp, horizontal = 8.dp)

    ) {
        if (url != null) {
            Icon(
                imageVector = Icons.Default.Link,
                contentDescription = "Open Link",
                Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(3.dp))
        }
        Text(text = name, style = MaterialTheme.typography.caption)
    }
}