/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.search.substance.roa.duration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.data.substances.classes.roa.DurationRange
import com.isaakhanimann.journal.data.substances.classes.roa.RoaDuration
import com.isaakhanimann.journal.ui.theme.JournalTheme

@Preview(showBackground = true)
@Composable
fun RoaDurationPreview(
    @PreviewParameter(RoaDurationPreviewProvider::class) roaDuration: RoaDuration
) {
    JournalTheme {
        RoaDurationView(roaDuration = roaDuration)
    }
}

@Composable
fun RoaDurationView(roaDuration: RoaDuration) {
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
            TimeSurface(durationRange = onset, name = "onset")
            TimeSurface(durationRange = comeup, name = "comeup")
            TimeSurface(durationRange = peak, name = "peak")
            TimeSurface(durationRange = offset, name = "offset")
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
    }
}

@Composable
fun TimeSurface(durationRange: DurationRange?, name: String) {
    if (durationRange != null) {
        Surface(shape = RoundedCornerShape(5.dp), tonalElevation = 12.dp) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(
                    horizontal = 7.dp,
                    vertical = 2.dp
                )
            ) {
                Text(durationRange.text)
                Text(name)
            }
        }
    }
}