package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding
import com.isaakhanimann.healthassistant.ui.theme.verticalPaddingCards

@Composable
fun CollapsibleSection(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.padding(
            horizontal = horizontalPadding,
            vertical = verticalPaddingCards
        )
    ) {
        var isExpanded by remember { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { isExpanded = !isExpanded }
                .padding(horizontal = horizontalPadding, vertical = 8.dp)
                .fillMaxWidth()
        ) {
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            if (isExpanded) {
                Icon(
                    imageVector = Icons.Outlined.ExpandLess,
                    contentDescription = "Expand Less",
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.ExpandMore,
                    contentDescription = "Expand More",
                )
            }

        }
        val expandTransition = remember {
            expandVertically(
                expandFrom = Alignment.Top,
                animationSpec = tween(300)
            ) + fadeIn(
                animationSpec = tween(300)
            )
        }
        val collapseTransition = remember {
            shrinkVertically(
                shrinkTowards = Alignment.Top,
                animationSpec = tween(300)
            ) + fadeOut(
                animationSpec = tween(300)
            )
        }
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandTransition,
            exit = collapseTransition
        ) {
            Column {
                Divider()
                content()
            }
        }
    }
}