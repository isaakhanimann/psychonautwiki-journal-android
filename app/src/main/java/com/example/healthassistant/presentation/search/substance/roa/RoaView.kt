package com.example.healthassistant.presentation.search.substance.roa

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Roa
import com.example.healthassistant.data.substances.RoaDose
import com.example.healthassistant.data.substances.RoaDuration

@Preview
@Composable
fun RoaView(@PreviewParameter(SampleRoaProvider::class) roa: Roa) {
    Card(modifier = Modifier
        .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = roa.name, style = MaterialTheme.typography.h5)
            roa.roaDose?.let {
                DoseView(roaDose = it)
                Divider()
            }
            roa.roaDuration?.let {
                DurationView(roaDuration = it)
            }
        }
    }
}

@Composable
fun DoseView(roaDose: RoaDose) {
    val lightMaxOrCommonMin = roaDose.light?.max ?: roaDose.common?.min
    val commonMaxOrStrongMin = roaDose.common?.max ?: roaDose.strong?.min
    val strongMaxOrHeavy = roaDose.strong?.max ?: roaDose.heavy
    Row(verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = roaDose.threshold.toString())
            Text("thresh  ")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-")
            Text("light")
        }
        Text(text = lightMaxOrCommonMin.toString())
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-")
            Text("common")
        }
        Text(text = commonMaxOrStrongMin.toString())
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-")
            Text("strong")
        }
        Text(text = strongMaxOrHeavy.toString())
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("-")
            Text("heavy")
        }
        Text(text = roaDose.units ?: "")
    }
}

@Composable
fun DurationView(roaDuration: RoaDuration) {
    Column {
        Text("total: ${roaDuration.total?.text}")
        Text("after effects: ${roaDuration.afterglow?.text}")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Column {
                LineLow()
                Text(roaDuration.onset?.text ?: "")
            }
            Column {
                LineUp()
                Text(roaDuration.comeup?.text ?: "")
            }
            Column {
                LineHigh()
                Text(roaDuration.peak?.text ?: "")
            }
            Column {
                LineDown()
                Text(roaDuration.offset?.text ?: "")
            }
        }
    }
}

@Composable
fun LineLow() {
Text(text = "onset")
}

@Composable
fun LineUp() {
    Text(text = "comeup")
}

@Composable
fun LineHigh() {
    Text(text = "peak")
}

@Composable
fun LineDown() {
    Text(text = "offset")
}