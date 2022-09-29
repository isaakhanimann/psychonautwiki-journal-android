package com.isaakhanimann.healthassistant.ui.search.substance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.InteractionType
import com.isaakhanimann.healthassistant.data.substances.classes.Interactions

@Preview
@Composable
fun InteractionsPreview(@PreviewParameter(InteractionsPreviewProvider::class) interactions: Interactions) {
    InteractionsView(
        interactions,
        titleStyle = MaterialTheme.typography.h6
    )
}

@Composable
fun InteractionsView(
    interactions: Interactions?,
    titleStyle: TextStyle
) {
    if (interactions != null) {
        if (interactions.dangerous.isNotEmpty() || interactions.unsafe.isNotEmpty() || interactions.uncertain.isNotEmpty()) {
            Column {
                Text(text = "Interactions", style = titleStyle)
                Spacer(modifier = Modifier.height(2.dp))
                if (interactions.dangerous.isNotEmpty()) {
                    interactions.dangerous.forEach {
                        InteractionRow(text = it, interactionType = InteractionType.DANGEROUS)
                    }
                }
                if (interactions.unsafe.isNotEmpty()) {
                    interactions.unsafe.forEach {
                        InteractionRow(text = it, interactionType = InteractionType.UNSAFE)

                    }
                }
                if (interactions.uncertain.isNotEmpty()) {
                    interactions.uncertain.forEach {
                        InteractionRow(text = it, interactionType = InteractionType.UNCERTAIN)
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "Check the PsychonautWiki article for explanations")
                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

@Composable
fun InteractionRow(
    text: String,
    interactionType: InteractionType,
    verticalSpaceBetween: Dp = 1.dp,
    verticalPaddingInside: Dp = 2.dp,
    textStyle: TextStyle = MaterialTheme.typography.body1
) {
    Surface(
        modifier = Modifier
            .padding(vertical = verticalSpaceBetween)
            .fillMaxWidth(),
        shape = RoundedCornerShape(2.dp),
        color = interactionType.color
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 5.dp, vertical = verticalPaddingInside),
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