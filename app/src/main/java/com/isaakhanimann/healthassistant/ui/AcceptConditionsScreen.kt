package com.isaakhanimann.healthassistant.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.R

@Preview
@Composable
fun AcceptConditionsPreview() {
    AcceptConditionsScreen {}
}

@Composable
fun AcceptConditionsScreen(
    onTapAccept: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.padding(10.dp)
    ) {
        val checkedState0 = remember { mutableStateOf(false) }
        val checkedState1 = remember { mutableStateOf(false) }
        val checkedState2 = remember { mutableStateOf(false) }
        val checkedState3 = remember { mutableStateOf(false) }
        val allIsChecked =
            checkedState0.value && checkedState1.value && checkedState2.value && checkedState3.value
        val painter = if (allIsChecked) painterResource(R.drawable.eye_open) else painterResource(R.drawable.eye_closed)
        Image(
            painter = painter,
            contentDescription = "PsychonautWiki eye",
            modifier = Modifier.fillMaxWidth(0.4f)
        )
        Column(horizontalAlignment = Alignment.Start) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedState0.value,
                    onCheckedChange = { checkedState0.value = it }
                )
                Text(text = "I acknowledge that I am the only one responsible for my actions, especially when deciding to use drugs")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedState1.value,
                    onCheckedChange = { checkedState1.value = it }
                )
                Text(text = "I'm going to use this app for mitigating the risks of my or somebody else’s substance use")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedState2.value,
                    onCheckedChange = { checkedState2.value = it }
                )
                Text(text = "I acknowledge that the data in this app might be inaccurate or incomplete")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = checkedState3.value,
                    onCheckedChange = { checkedState3.value = it }
                )
                Text(text = "I’m going to seek professional help before attempting to self-medicate")
            }
        }
        Text(text = "Journaling data in the app always stays on this device", style = MaterialTheme.typography.caption)
        Button(onClick = onTapAccept, enabled = allIsChecked) {
            Text(text = "Continue")
        }
    }
}