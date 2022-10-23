package com.isaakhanimann.healthassistant.ui.search.substance.roa.duration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.healthassistant.ui.theme.HealthAssistantTheme

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProvider::class) roaDuration: RoaDuration
) {
    HealthAssistantTheme {
        RoaDurationView(roaDuration = roaDuration, isOralRoute = true)
    }
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
            val shapePaddingHorizontal = 7.dp
            val shapePaddingVertical = 2.dp
            val cornerRadius = 5.dp
            if (onset != null) {
                Surface(shape = RoundedCornerShape(cornerRadius)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(
                            horizontal = shapePaddingHorizontal,
                            vertical = shapePaddingVertical
                        )
                    ) {
                        Text(
                            onset.text + if (isOralRoute) "*" else "",
                        )
                        Text("onset")
                    }
                }
            }
            if (comeup != null) {
                Surface(shape = RoundedCornerShape(cornerRadius)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(
                            horizontal = shapePaddingHorizontal,
                            vertical = shapePaddingVertical
                        )
                    ) {
                        Text(comeup.text)
                        Text("comeup")
                    }
                }
            }
            if (peak != null) {
                Surface(shape = RoundedCornerShape(cornerRadius)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(
                            horizontal = shapePaddingHorizontal,
                            vertical = shapePaddingVertical
                        )
                    ) {
                        Text(peak.text)
                        Text("peak")
                    }
                }
            }
            if (offset != null) {
                Surface(shape = RoundedCornerShape(cornerRadius)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(
                            horizontal = shapePaddingHorizontal,
                            vertical = shapePaddingVertical
                        )
                    ) {
                        Text(offset.text)
                        Text("offset")
                    }
                }
            }
        }
        if (total != null || afterglow != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
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