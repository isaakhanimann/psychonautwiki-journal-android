package com.isaakhanimann.healthassistant.ui.addingestion.time

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun DatePickerButton(
    day: Int,
    month: Int,
    year: Int,
    onSubmitDate: (Int, Int, Int) -> Unit,
    dateString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, newYear: Int, newMonth: Int, newDay: Int ->
            onSubmitDate(newDay, newMonth + 1, newYear)
        }, year, month - 1, day
    )
    OutlinedButton(onClick = datePickerDialog::show, modifier = modifier) {
        Text(dateString)
    }
}