package com.example.healthassistant.ui.search.substance.roa

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.healthassistant.data.substances.Tolerance

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
        titleStyle = MaterialTheme.typography.subtitle2
    )
}

@Composable
fun ToleranceSection(
    tolerance: Tolerance?,
    crossTolerances: List<String>,
    titleStyle: TextStyle
) {
    if (tolerance != null || crossTolerances.isNotEmpty()) {
        Column {
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Tolerance", style = titleStyle)
            if (tolerance != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {

                        val labelWidth = 40.dp
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (tolerance.zero != null) {
                                Text(
                                    text = "zero",
                                    style = MaterialTheme.typography.subtitle2,
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.zero)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (tolerance.half != null) {
                                Text(
                                    text = "half",
                                    style = MaterialTheme.typography.subtitle2,
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.half)
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            if (tolerance.full != null) {
                                Text(
                                    text = "full",
                                    style = MaterialTheme.typography.subtitle2,
                                    modifier = Modifier.width(labelWidth)
                                )
                                Text(text = tolerance.full)
                            }
                        }
                    }
                    var isShowingDialog by remember { mutableStateOf(false) }
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "Tolerance Info",
                        Modifier
                            .size(ButtonDefaults.IconSize)
                            .clickable {
                                isShowingDialog = true
                            }
                    )
                    if (isShowingDialog) {
                        AlertDialog(
                            onDismissRequest = { isShowingDialog = false },
                            title = {
                                Text(
                                    text = "Tolerance",
                                    style = MaterialTheme.typography.h6,
                                )
                            },
                            text = {
                                Text(text = "Zero tolerance refers to the time until there is no tolerance anymore. Half tolerance to the time until there is half the tolerance.")
                            },
                            buttons = {
                                Row(
                                    modifier = Modifier.padding(all = 8.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    TextButton(
                                        modifier = Modifier.fillMaxWidth(),
                                        onClick = { isShowingDialog = false }
                                    ) {
                                        Text("Dismiss")
                                    }
                                }
                            },
                        )
                    }
                }
            }
            if (crossTolerances.isNotEmpty()) {
                val names = crossTolerances.map { it }.distinct()
                    .joinToString(separator = ", ")
                Text(text = "Cross tolerance with $names")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider()
        }

    }

}