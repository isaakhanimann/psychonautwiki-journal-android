package com.example.healthassistant.ui.addingestion.time

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.data.room.experiences.entities.SubstanceColor


@Composable
fun ChooseTimeScreen(
    dismissAddIngestionScreens: () -> Unit,
    viewModel: ChooseTimeViewModel = hiltViewModel()
) {
    ChooseTimeScreen(
        createAndSaveIngestion = viewModel::createAndSaveIngestion,
        onSubmitDate = viewModel::onSubmitDate,
        onSubmitTime = viewModel::onSubmitTime,
        dateAndTime = DateAndTime(
            day = viewModel.day.value,
            month = viewModel.month.value,
            year = viewModel.year.value,
            hour = viewModel.hour.value,
            minute = viewModel.minute.value
        ),
        dateAndTimeStrings = Pair(viewModel.dateString, viewModel.timeString),
        dismissAddIngestionScreens = dismissAddIngestionScreens,
        selectedColor = viewModel.selectedColor,
        onChangeColor = { viewModel.selectedColor = it }
    )
}

@Preview
@Composable
fun ChooseTimeScreenPreview() {
    ChooseTimeScreen(
        createAndSaveIngestion = {},
        onSubmitDate = { _: Int, _: Int, _: Int -> },
        onSubmitTime = { _: Int, _: Int -> },
        dateAndTime = DateAndTime(day = 3, month = 4, year = 2022, hour = 13, minute = 52),
        dateAndTimeStrings = Pair("Wed 9 Jul 2022", "13:52"),
        dismissAddIngestionScreens = {},
        selectedColor = SubstanceColor.BLUE,
        onChangeColor = {}
    )
}

data class DateAndTime(val day: Int, val month: Int, val year: Int, val hour: Int, val minute: Int)

@Composable
fun ChooseTimeScreen(
    createAndSaveIngestion: () -> Unit,
    onSubmitDate: (Int, Int, Int) -> Unit,
    onSubmitTime: (Int, Int) -> Unit,
    dateAndTime: DateAndTime,
    dateAndTimeStrings: Pair<String, String>,
    dismissAddIngestionScreens: () -> Unit,
    selectedColor: SubstanceColor,
    onChangeColor: (SubstanceColor) -> Unit
) {
    Column {
        LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth())
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Choose Ingestion Time") }) },
            floatingActionButton = {
                val context = LocalContext.current
                ExtendedFloatingActionButton(
                    onClick = {
                        createAndSaveIngestion()
                        Toast.makeText(
                            context,
                            "Ingestion Added",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismissAddIngestionScreens()
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Next"
                        )
                    },
                    text = { Text("Done") },
                )
            }
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                ColorPicker(
                    selectedColor = selectedColor,
                    onChangeOfColor = onChangeColor
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DatePickerButton(
                        day = dateAndTime.day,
                        month = dateAndTime.month,
                        year = dateAndTime.year,
                        onSubmitDate = onSubmitDate,
                        dateString = dateAndTimeStrings.first,
                        modifier = Modifier.fillMaxWidth()
                    )
                    TimePickerButton(
                        hour = dateAndTime.hour,
                        minute = dateAndTime.minute,
                        onSubmitTime = onSubmitTime,
                        timeString = dateAndTimeStrings.second,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun TimePickerButton(
    hour: Int,
    minute: Int,
    onSubmitTime: (Int, Int) -> Unit,
    timeString: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, newHour: Int, newMinute: Int ->
            onSubmitTime(newHour, newMinute)
        }, hour, minute, true
    )
    OutlinedButton(onClick = timePickerDialog::show, modifier = modifier) {
        Text(timeString)
    }
}