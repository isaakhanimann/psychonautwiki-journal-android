package com.isaakhanimann.healthassistant.ui.search.substance.roa.duration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProvider::class, limit = 1) roaDuration: RoaDuration
) {
    RoaDurationView(roaDuration = roaDuration, isOralRoute = true)
}

@Composable
fun RoaDurationView(
    roaDuration: RoaDuration,
    isOralRoute: Boolean
) {
    Column {
        val total = roaDuration.total
        val afterglow = roaDuration.afterglow
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val onset = roaDuration.onset
            val comeup = roaDuration.comeup
            val peak = roaDuration.peak
            val offset = roaDuration.offset
            if (onset != null) {
                Column {
                    Text(
                        onset.text + if (isOralRoute) "*" else "",
                    )
                    Text("onset")
                }
                Text("-")
            }
            if (comeup != null) {
                Column {
                    Text(comeup.text)
                    Text("comeup")
                }
                Text("-")
            }
            if (peak != null) {
                Column {
                    Text(peak.text)
                    Text("peak")
                }
                Text("-")
            }
            if (offset != null) {
                Column {
                    Text(offset.text)
                    Text("offset")
                }
            }
        }
        if (total != null || afterglow != null) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                if (total != null) {
                    Text("total: ${total.text}")
                }
                if (afterglow != null) {
                    Text("after effects: ${afterglow.text}")
                }
            }
        }
        if (isOralRoute) {
            Text(
                text = "* a full stomach can delay the onset for hours",
            )
        }
    }
}