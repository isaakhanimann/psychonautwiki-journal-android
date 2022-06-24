package com.example.healthassistant.ui.experiences.experience.addingestion.time

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun ChooseTimeScreen(
    popUpToExperienceScreen: () -> Unit,
    popUpToSubstanceScreen: () -> Unit,
    viewModel: ChooseTimeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    ChooseTimeScreenContent(
        addIngestionToExperience = {
            viewModel.createAndSaveIngestion(experienceIdToAddTo = it)
        },
        addIngestionToNewExperience = {
            viewModel.addIngestionToNewExperience {
                Toast.makeText(
                    context, "Ingestion Added",
                    Toast.LENGTH_SHORT
                ).show()
                popUpToSubstanceScreen()
            }
        },
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
        ),
        dateAndTimeStrings = Pair(viewModel.dateString, viewModel.timeString),
        popUpToExperienceScreen = popUpToExperienceScreen,
        popUpToSubstanceScreen = popUpToSubstanceScreen
    )
}

data class DateAndTime(val day: Int, val month: Int, val year: Int, val hour: Int, val minute: Int)

@Preview
@Composable
fun ChooseTimeScreenContent(
    addIngestionToExperience: (Int) -> Unit = {},
    addIngestionToNewExperience: () -> Unit = {},
    experienceIdToAddTo: Int? = null,
    latestExperienceId: Int? = null,
    onSubmitDate: (Int, Int, Int) -> Unit = { _: Int, _: Int, _: Int -> },
    onSubmitTime: (Int, Int) -> Unit = { _: Int, _: Int -> },
    dateAndTime: DateAndTime = DateAndTime(day = 3, month = 4, year = 2022, hour = 13, minute = 52),
    dateAndTimeStrings: Pair<String, String> = Pair("Wed 9 Jul 2022", "13:52"),
    popUpToExperienceScreen: () -> Unit = {},
    popUpToSubstanceScreen: () -> Unit = {},
) {
    Column {
        LinearProgressIndicator(progress = 1f, modifier = Modifier.fillMaxWidth())
        Scaffold(
            topBar = { TopAppBar(title = { Text(text = "Choose Ingestion Time") }) }
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
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
                AddIngestionButtons(
                    addIngestionToExperience = addIngestionToExperience,
                    addIngestionToNewExperience = addIngestionToNewExperience,
                    experienceIdToAddTo = experienceIdToAddTo,
                    latestExperienceId = latestExperienceId,
                    popUpToSubstanceScreen = popUpToSubstanceScreen,
                    popUpToExperienceScreen = popUpToExperienceScreen
                )
            }
        }
    }
}

@Composable
fun AddIngestionButtons(
    addIngestionToExperience: (Int) -> Unit,
    addIngestionToNewExperience: () -> Unit,
    experienceIdToAddTo: Int?,
    latestExperienceId: Int?,
    popUpToSubstanceScreen: () -> Unit,
    popUpToExperienceScreen: () -> Unit,
) {
    val context = LocalContext.current
    if (experienceIdToAddTo != null) {
        Button(
            onClick = {
                addIngestionToExperience(experienceIdToAddTo)
                Toast.makeText(
                    context, "Ingestion Added",
                    Toast.LENGTH_SHORT
                ).show()
                popUpToExperienceScreen()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Add Ingestion")
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (latestExperienceId != null) {
                Button(
                    onClick = {
                        addIngestionToExperience(latestExperienceId)
                        Toast.makeText(
                            context, "Ingestion Added",
                            Toast.LENGTH_SHORT
                        ).show()
                        popUpToSubstanceScreen()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Add To Latest Experience")
                }
            }
            Button(onClick = addIngestionToNewExperience, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Add To New Experience")
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