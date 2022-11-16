/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.journal.addingestion.time

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
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
fun DatePickerButton(
    localDateTime: LocalDateTime,
    onChange: (LocalDateTime) -> Unit,
    dateString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dialogTheme =
        if (isSystemInDarkTheme()) R.style.DialogThemeDark else R.style.DialogThemeLight
    val datePickerDialog = DatePickerDialog(
        context,
        dialogTheme,
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
        Icon(
            Icons.Outlined.Event,
            contentDescription = "Open Calendar"
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(dateString)
    }
}