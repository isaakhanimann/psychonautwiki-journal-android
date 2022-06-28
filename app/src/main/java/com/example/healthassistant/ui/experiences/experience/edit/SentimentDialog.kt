package com.example.healthassistant.ui.experiences.experience.edit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.room.experiences.entities.Sentiment

@Preview
@Composable
fun SentimentDialogPreview() {
    SentimentDialog(
        dismiss = {},
        saveSentiment = {}
    )
}

@Composable
fun SentimentDialog(
    dismiss: () -> Unit,
    saveSentiment: (sentiment: Sentiment?) -> Unit,
) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(text = "Choose a Sentiment", style = MaterialTheme.typography.h6)
        },
        text = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                val iconSize = 40.dp
                Sentiment.values().forEach { sentiment ->
                    IconButton(
                        onClick = {
                            saveSentiment(sentiment)
                            dismiss()
                        }
                    ) {
                        Icon(
                            imageVector = sentiment.icon,
                            contentDescription = sentiment.description,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
                IconButton(
                    onClick = {
                        saveSentiment(null)
                        dismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Remove sentiment",
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        },
        buttons = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = dismiss
            ) {
                Text("Cancel", textAlign = TextAlign.Center)
            }
        },
    )
}