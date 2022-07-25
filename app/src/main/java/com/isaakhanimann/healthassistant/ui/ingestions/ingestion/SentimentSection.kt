package com.isaakhanimann.healthassistant.ui.ingestions.ingestion

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment

@Preview
@Composable
fun SentimentSectionPreview() {
    SentimentSection(
        sentiment = Sentiment.SATISFIED,
        isShowingEditSentiment = false,
        show = {},
        dismiss = {},
        saveSentiment = {}
    )
}

@Composable
fun SentimentSection(
    sentiment: Sentiment?,
    isShowingEditSentiment: Boolean,
    show: () -> Unit,
    dismiss: () -> Unit,
    saveSentiment: (Sentiment?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (sentiment == null) {
        Box(
            modifier = Modifier
                .wrapContentSize(Alignment.TopEnd)
        ) {
            TextButton(
                onClick = show,
            ) {
                Icon(
                    Icons.Filled.AddReaction,
                    contentDescription = "Add Sentiment",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Add Sentiment")
            }
            SentimentDropDownMenu(
                isShowingEditSentiment = isShowingEditSentiment,
                dismiss = dismiss,
                saveSentiment = saveSentiment
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(text = "Overall Sentiment")
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                sentiment.icon,
                contentDescription = sentiment.description,
                modifier = Modifier
                    .size(30.dp),
            )
            Text(text = sentiment.description)
            Box(
                modifier = Modifier
                    .wrapContentSize(Alignment.TopEnd)
            ) {
                IconButton(onClick = show) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Sentiment",
                        Modifier.size(ButtonDefaults.IconSize)
                    )
                }
                SentimentDropDownMenu(
                    isShowingEditSentiment = isShowingEditSentiment,
                    dismiss = dismiss,
                    saveSentiment = saveSentiment
                )
            }
        }
    }
}

@Composable
fun SentimentDropDownMenu(
    isShowingEditSentiment: Boolean,
    dismiss: () -> Unit,
    saveSentiment: (Sentiment?) -> Unit
) {
    DropdownMenu(
        expanded = isShowingEditSentiment,
        onDismissRequest = dismiss
    ) {
        Sentiment.values().forEach { sentiment ->
            DropdownMenuItem(
                onClick = {
                    saveSentiment(sentiment)
                    dismiss()
                },
            ) {
                Icon(
                    sentiment.icon,
                    contentDescription = sentiment.description,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(sentiment.description)
            }
        }
        DropdownMenuItem(
            onClick = {
                saveSentiment(null)
                dismiss()
            },
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = "None",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("None")
        }
    }
}
