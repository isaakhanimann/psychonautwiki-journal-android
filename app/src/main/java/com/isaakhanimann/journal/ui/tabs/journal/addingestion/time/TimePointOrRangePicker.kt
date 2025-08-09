package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.isaakhanimann.journal.R
import com.isaakhanimann.journal.ui.utils.getDateWithWeekdayText
import com.isaakhanimann.journal.ui.utils.getShortTimeText
import java.time.LocalDateTime

@OptIn(ExperimentalLayoutApi::class)
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
            Text(stringResource(R.string.point_in_time))
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            onClick = { onChangeTimePickerOption(IngestionTimePickerOption.TIME_RANGE) },
            selected = ingestionTimePickerOption == IngestionTimePickerOption.TIME_RANGE
        ) {
            Text(stringResource(R.string.time_range))
        }
    }
    AnimatedContent(
        targetState = ingestionTimePickerOption,
        label = "ingestionTimePicker"
    ) { option ->
        when (option) {
            IngestionTimePickerOption.POINT_IN_TIME -> {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    DatePickerButton(
                        localDateTime = localDateTimeStart,
                        onChange = onChangeStartDateOrTime,
                        dateString = localDateTimeStart.getDateWithWeekdayText(),
                    )
                    TimePickerButton(
                        localDateTime = localDateTimeStart,
                        onChange = onChangeStartDateOrTime,
                        timeString = localDateTimeStart.getShortTimeText(),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        onChangeStartDateOrTime(LocalDateTime.now())
                    }) {
                        Icon(
                            Icons.Default.Update,
                            contentDescription = stringResource(R.string.update_time_to_now),
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
                        Text(stringResource(R.string.start))
                        IconButton(onClick = {
                            onChangeStartDateOrTime(LocalDateTime.now())
                        }) {
                            Icon(
                                Icons.Default.Update,
                                contentDescription = stringResource(R.string.update_time_to_now),
                            )
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        DatePickerButton(
                            localDateTime = localDateTimeStart,
                            onChange = onChangeStartDateOrTime,
                            dateString = localDateTimeStart.getDateWithWeekdayText(),
                        )
                        TimePickerButton(
                            localDateTime = localDateTimeStart,
                            onChange = onChangeStartDateOrTime,
                            timeString = localDateTimeStart.getShortTimeText(),
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
                                contentDescription = stringResource(R.string.update_time_to_now),
                            )
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        DatePickerButton(
                            localDateTime = localDateTimeEnd,
                            onChange = onChangeEndDateOrTime,
                            dateString = localDateTimeEnd.getDateWithWeekdayText(),
                        )
                        TimePickerButton(
                            localDateTime = localDateTimeEnd,
                            onChange = onChangeEndDateOrTime,
                            timeString = localDateTimeEnd.getShortTimeText(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}