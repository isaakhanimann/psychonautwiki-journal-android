package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    InteractionsView(interactions)
}

@Composable
fun InteractionsView(interactions: Interactions) {
    Column {
        if (interactions.dangerous.isNotEmpty()) {
            interactions.dangerous.forEach {
                InteractionRowDividers(text = it, interactionType = InteractionType.DANGEROUS)
                Divider()
            }
        }
        if (interactions.unsafe.isNotEmpty()) {
            interactions.unsafe.forEach {
                InteractionRowDividers(text = it, interactionType = InteractionType.UNSAFE)
                Divider()
            }
        }
        if (interactions.uncertain.isNotEmpty()) {
            interactions.uncertain.forEach {
                InteractionRowDividers(text = it, interactionType = InteractionType.UNCERTAIN)
                Divider()
            }
        }
        Text(
            text = "Check the PsychonautWiki article for explanations",
            modifier = Modifier.padding(horizontal = horizontalPadding)
        )
        VerticalSpace()
    }

}

@Composable
fun InteractionRowDividers(
    text: String,
    interactionType: InteractionType,
    verticalPaddingInside: Dp = 2.dp,
    textStyle: TextStyle = MaterialTheme.typography.body1
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RectangleShape,
        color = interactionType.color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = horizontalPadding, vertical = verticalPaddingInside),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = textStyle,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.weight(1f))
            LazyRow {
                items(interactionType.dangerCount) {
                    Icon(
                        imageVector = Icons.Outlined.WarningAmber,
                        contentDescription = "Warning",
                        tint = Color.Black,
                    )
                }
            }
        }
    }
}