package com.isaakhanimann.journal.ui.addingestion.time

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDateTime

@Composable
fun DatePickerButton(
    localDateTime: LocalDateTime,
    onChange: (LocalDateTime) -> Unit,
    dateString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, newYear: Int, newMonth: Int, newDay: Int ->
            onChange(
                LocalDateTime.now()
                    .withYear(newYear)
                    .withMonth(newMonth + 1)
                    .withDayOfMonth(newDay)
                    .withHour(localDateTime.hour)
                    .withMinute(localDateTime.minute)
            )
        }, localDateTime.year, localDateTime.monthValue - 1, localDateTime.dayOfMonth
    )
    OutlinedButton(onClick = datePickerDialog::show, modifier = modifier) {
        Text(dateString)
    }
}