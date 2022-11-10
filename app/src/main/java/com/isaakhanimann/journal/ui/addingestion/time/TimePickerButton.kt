package com.isaakhanimann.journal.ui.addingestion.time

import android.app.TimePickerDialog
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.isaakhanimann.journal.R
import java.time.LocalDateTime

@Composable
fun TimePickerButton(
    localDateTime: LocalDateTime,
    onChange: (LocalDateTime) -> Unit,
    timeString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dialogTheme =
        if (isSystemInDarkTheme()) R.style.DialogThemeDark else R.style.DialogThemeLight
    val timePickerDialog = TimePickerDialog(
        context,
        dialogTheme,
        { _, newHour: Int, newMinute: Int ->
            onChange(
                LocalDateTime.now()
                    .withYear(localDateTime.year)
                    .withMonth(localDateTime.monthValue)
                    .withDayOfMonth(localDateTime.dayOfMonth)
                    .withHour(newHour)
                    .withMinute(newMinute)
            )
        }, localDateTime.hour, localDateTime.minute, false
    )
    OutlinedButton(onClick = timePickerDialog::show, modifier = modifier) {
        Icon(
            Icons.Outlined.Schedule,
            contentDescription = "Open Time Picker"
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(timeString)
    }
}