package com.isaakhanimann.healthassistant.ui.journal.experience

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddReaction
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Sentiment

@Preview
@Composable
fun SentimentSectionPreview() {
    SentimentButton(
        sentiment = Sentiment.SATISFIED,
        saveSentiment = {}
    )
}

@Composable
fun SentimentButton(
    sentiment: Sentiment?,
    saveSentiment: (Sentiment?) -> Unit,
) {
    var isShowingSentimentMenu by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
    ) {
        IconButton(onClick = { isShowingSentimentMenu = true }) {
            val icon = sentiment?.icon ?: Icons.Filled.AddReaction
            Icon(icon, contentDescription = "Add Sentiment")
        }
        SentimentDropDownMenu(
            isShowingEditSentiment = isShowingSentimentMenu,
            dismiss = { isShowingSentimentMenu = false },
            saveSentiment = saveSentiment
        )
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
