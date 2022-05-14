package com.example.healthassistant.ui.home.experience.addingestion.time

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.ui.main.routers.navigateToExperience


@Composable
fun ChooseTimeScreen(
    navController: NavController,
    viewModel: ChooseTimeViewModel = hiltViewModel()
) {
    ChooseTimeScreenContent(
        addIngestionAndNavigate = {
            viewModel.addIngestion(experienceIdToAddTo = it)
            navController.navigateToExperience(experienceId = it)
        },
        addIngestionToNewExperienceAndNavigate = viewModel::addIngestionToNewExperience,
        experienceIdToAddTo = viewModel.experienceId,
        latestExperienceId = viewModel.latestExperience?.id,
        onSubmitDate = viewModel::onSubmitDate,
        onSubmitTime = viewModel::onSubmitTime,
        dateAndTime = DateAndTime(
            day = viewModel.day.value,
            month = viewModel.month.value,
            year = viewModel.year.value,
            hour = viewModel.hour.value,
            minute = viewModel.minute.value
        )
    )
}

data class DateAndTime(val day: Int, val month: Int, val year: Int, val hour: Int, val minute: Int)

@Preview
@Composable
fun ChooseTimeScreenContent(
    addIngestionAndNavigate: (Int) -> Unit = {},
    addIngestionToNewExperienceAndNavigate: () -> Unit = {},
    experienceIdToAddTo: Int? = null,
    latestExperienceId: Int? = null,
    onSubmitDate: (Int, Int, Int) -> Unit = { _: Int, _: Int, _: Int -> },
    onSubmitTime: (Int, Int) -> Unit = { _: Int, _: Int -> },
    dateAndTime: DateAndTime = DateAndTime(day = 3, month = 4, year = 2022, hour = 13, minute = 52)
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Ingestion Time") }) }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxHeight().padding(10.dp)
        ) {
            Row(modifier = Modifier.background(Color.Red)) {
                DatePickerButton(
                    day = dateAndTime.day,
                    month = dateAndTime.month,
                    year = dateAndTime.year,
                    onSubmitDate = onSubmitDate
                )
                TimePickerButton(
                    hour = dateAndTime.hour,
                    minute = dateAndTime.minute,
                    onSubmitTime = onSubmitTime
                )
            }
            Text("Current datetime: ${dateAndTime.day}.${dateAndTime.month}.${dateAndTime.year} at ${dateAndTime.hour}:${dateAndTime.minute}")
            AddIngestionButtons(
                addIngestionAndNavigate = addIngestionAndNavigate,
                addIngestionToNewExperienceAndNavigate = addIngestionToNewExperienceAndNavigate,
                experienceIdToAddTo = experienceIdToAddTo,
                latestExperienceId = latestExperienceId
            )
        }
    }
}

@Composable
fun AddIngestionButtons(
    addIngestionAndNavigate: (Int) -> Unit,
    addIngestionToNewExperienceAndNavigate: () -> Unit,
    experienceIdToAddTo: Int?,
    latestExperienceId: Int?,
) {
    if (experienceIdToAddTo != null) {
        Button(onClick = { addIngestionAndNavigate(experienceIdToAddTo) }) {
            Text(text = "Add Ingestion")
        }
    } else {
        if (latestExperienceId != null) {
            Column {
                Button(onClick = { addIngestionAndNavigate(latestExperienceId) }) {
                    Text(text = "Add Ingestion To Latest Experience")
                }
                Button(onClick = addIngestionToNewExperienceAndNavigate) {
                    Text(text = "Add Ingestion To New Experience")
                }
            }
        } else {
            Button(onClick = addIngestionToNewExperienceAndNavigate) {
                Text(text = "Add Ingestion To New Experience")
            }
        }
    }
}

@Composable
fun TimePickerButton(
    hour: Int,
    minute: Int,
    onSubmitTime: (Int, Int) -> Unit
) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, newHour: Int, newMinute: Int ->
            onSubmitTime(newHour, newMinute)
        }, hour, minute, true
    )
    Button(onClick = timePickerDialog::show) {
        Text(text = "Change Time")
    }
}

@Composable
fun DatePickerButton(
    day: Int,
    month: Int,
    year: Int,
    onSubmitDate: (Int, Int, Int) -> Unit
) {
    val context = LocalContext.current
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, newYear: Int, newMonth: Int, newDay: Int ->
            onSubmitDate(newDay, newMonth, newYear)
        }, year, month, day
    )
    Button(onClick = datePickerDialog::show) {
        Text(text = "Change Date")
    }
}