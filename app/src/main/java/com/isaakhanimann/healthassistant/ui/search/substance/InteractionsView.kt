package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.InteractionType
import com.isaakhanimann.healthassistant.data.substances.classes.Interactions
import com.isaakhanimann.healthassistant.ui.theme.horizontalPadding

@Preview
@Composable
fun InteractionsPreview(@PreviewParameter(InteractionsPreviewProvider::class) interactions: Interactions) {
    InteractionsView(interactions, substanceURL = "")
}

@Composable
fun InteractionsView(interactions: Interactions, substanceURL: String) {
    Column {
        if (interactions.dangerous.isNotEmpty()) {
            interactions.dangerous.forEach {
                InteractionRowSubstanceScreen(
                    text = it,
                    interactionType = InteractionType.DANGEROUS
                )
            }
        }
        if (interactions.unsafe.isNotEmpty()) {
            interactions.unsafe.forEach {
                InteractionRowSubstanceScreen(text = it, interactionType = InteractionType.UNSAFE)
            }
        }
        if (interactions.uncertain.isNotEmpty()) {
            interactions.uncertain.forEach {
                InteractionRowSubstanceScreen(
                    text = it,
                    interactionType = InteractionType.UNCERTAIN
                )
            }
        }
        InteractionExplanationButton(substanceURL = substanceURL)
    }
}

@Composable
fun InteractionExplanationButton(substanceURL: String) {
    val uriHandler = LocalUriHandler.current
    TextButton(onClick = {
        val interactionURL = "$substanceURL#Dangerous_interactions"
        uriHandler.openUri(interactionURL)
    }) {
        Icon(
            Icons.Default.OpenInBrowser,
            contentDescription = "Open Link"
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text("Interaction Explanations")
    }
}

@Composable
fun InteractionRowSubstanceScreen(
    text: String,
    interactionType: InteractionType,
    verticalPaddingInside: Dp = 2.dp
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 1.dp),
        shape = RectangleShape,
        color = interactionType.color
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = horizontalPadding,
                vertical = verticalPaddingInside
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            LazyRow {
                items(interactionType.dangerCount) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = "Warning",
                        tint = Color.Black,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }
    }
}