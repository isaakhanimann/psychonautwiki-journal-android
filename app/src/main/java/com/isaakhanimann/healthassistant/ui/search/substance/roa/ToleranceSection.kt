package com.isaakhanimann.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.isaakhanimann.healthassistant.data.substances.classes.Tolerance
import com.isaakhanimann.healthassistant.ui.search.substance.ArrowDown

@Preview(showBackground = true)
@Composable
fun ToleranceSectionPreview() {
    ToleranceSection(
        tolerance = Tolerance(
            full = "with prolonged use",
            half = "two weeks",
            zero = "1 month"
        ),
        crossTolerances = listOf(
            "dopamine",
            "stimulant"
        ),
    )
}

@Composable
fun ToleranceSection(
    tolerance: Tolerance?,
    crossTolerances: List<String>,
    modifier: Modifier = Modifier
) {
    if (tolerance != null || crossTolerances.isNotEmpty()) {
        Column(modifier) {
            if (tolerance != null) {
                val labelWidth = 40.dp
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ArrowDown(
                        strokeWidth = 4f,
                        fractionWhenHeadStarts = 9f/10,
                        width = 5.dp,
                        height = 30.dp
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (tolerance.full != null) {
                                Text(
                                    text = "full",
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.full)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (tolerance.half != null) {
                                Text(
                                    text = "half",
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.half)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (tolerance.zero != null) {
                                Text(
                                    text = "zero",
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.zero)
                            }
                        }
                    }
                }
                Text(text = "zero is the time until tolerance is like the first time", style = MaterialTheme.typography.caption)
            }
            if (crossTolerances.isNotEmpty()) {
                val names = crossTolerances.map { it }.distinct()
                    .joinToString(separator = ", ")
                Text(text = "Cross tolerance with $names")
            }
        }

    }

}