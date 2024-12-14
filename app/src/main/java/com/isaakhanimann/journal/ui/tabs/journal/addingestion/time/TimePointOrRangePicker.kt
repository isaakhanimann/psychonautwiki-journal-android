package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.ui.utils.getStringOfPattern
import java.time.LocalDateTime

@Composable
fun TimePointOrRangePicker(
    onChangeTimePickerOption: (option: IngestionTimePickerOption) -> Unit,
    ingestionTimePickerOption: IngestionTimePickerOption,
    localDateTimeStart: LocalDateTime,
    onChangeStartDateOrTime: (LocalDateTime) -> Unit,
    localDateTimeEnd: LocalDateTime,
    onChangeEndDateOrTime: (LocalDateTime) -> Unit
) {
    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            onClick = { onChangeTimePickerOption(IngestionTimePickerOption.POINT_IN_TIME) },
            selected = ingestionTimePickerOption == IngestionTimePickerOption.POINT_IN_TIME
        ) {
            Text("Point in time")
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            onClick = { onChangeTimePickerOption(IngestionTimePickerOption.TIME_RANGE) },
            selected = ingestionTimePickerOption == IngestionTimePickerOption.TIME_RANGE
        ) {
            Text("Time range")
        }
    }
    AnimatedContent(
        targetState = ingestionTimePickerOption,
        label = "ingestionTimePicker"
    ) { option ->
        when (option) {
            IngestionTimePickerOption.POINT_IN_TIME -> {
                Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    DatePickerButton(
                        localDateTime = localDateTimeStart,
                        onChange = onChangeStartDateOrTime,
                        dateString = localDateTimeStart.getStringOfPattern("EEE d MMM yyyy"),
                    )
                    TimePickerButton(
                        localDateTime = localDateTimeStart,
                        onChange = onChangeStartDateOrTime,
                        timeString = localDateTimeStart.getStringOfPattern("HH:mm"),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        onChangeStartDateOrTime(LocalDateTime.now())
                    }) {
                        Icon(
                            Icons.Default.Update,
                            contentDescription = "Update time to now",
                        )
                    }
                }
            }

            IngestionTimePickerOption.TIME_RANGE -> {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Start:")
                        IconButton(onClick = {
                            onChangeStartDateOrTime(LocalDateTime.now())
                        }) {
                            Icon(
                                Icons.Default.Update,
                                contentDescription = "Update time to now",
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DatePickerButton(
                            localDateTime = localDateTimeStart,
                            onChange = onChangeStartDateOrTime,
                            dateString = localDateTimeStart.getStringOfPattern("EEE d MMM yyyy"),
                        )
                        TimePickerButton(
                            localDateTime = localDateTimeStart,
                            onChange = onChangeStartDateOrTime,
                            timeString = localDateTimeStart.getStringOfPattern("HH:mm"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("End:")
                        IconButton(onClick = {
                            onChangeEndDateOrTime(LocalDateTime.now())
                        }) {
                            Icon(
                                Icons.Default.Update,
                                contentDescription = "Update time to now",
                            )
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DatePickerButton(
                            localDateTime = localDateTimeEnd,
                            onChange = onChangeEndDateOrTime,
                            dateString = localDateTimeEnd.getStringOfPattern("EEE d MMM yyyy"),
                        )
                        TimePickerButton(
                            localDateTime = localDateTimeEnd,
                            onChange = onChangeEndDateOrTime,
                            timeString = localDateTimeEnd.getStringOfPattern("HH:mm"),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}