package com.example.healthassistant.ui.home.experience.addingestion.time

import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.ui.main.routers.navigateToExperiencePopupToExperienceScreen
import com.example.healthassistant.ui.main.routers.navigateToExperiencePupupToSubstanceScreen


@Composable
fun ChooseTimeScreen(
    navController: NavController,
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
                navController.navigateToExperiencePupupToSubstanceScreen(it)
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
        dateString = viewModel.dateString,
        timeString = viewModel.timeString,
        navigateToExperiencePopUpToExperienceScreen = {
            navController.navigateToExperiencePopupToExperienceScreen(it)
        },
        navigateToExperiencePopUpToSubstanceScreen = {
            navController.navigateToExperiencePupupToSubstanceScreen(it)
        }
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
    dateString: String = "Wed 9 Jul 2022",
    timeString: String = "13:52",
    navigateToExperiencePopUpToSubstanceScreen: (Int) -> Unit = {},
    navigateToExperiencePopUpToExperienceScreen: (Int) -> Unit = {},
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Choose Ingestion Time") }) }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DatePickerButton(
                    day = dateAndTime.day,
                    month = dateAndTime.month,
                    year = dateAndTime.year,
                    onSubmitDate = onSubmitDate,
                    dateString = dateString
                )
                TimePickerButton(
                    hour = dateAndTime.hour,
                    minute = dateAndTime.minute,
                    onSubmitTime = onSubmitTime,
                    timeString = timeString
                )
            }
            AddIngestionButtons(
                addIngestionToExperience = addIngestionToExperience,
                addIngestionToNewExperience = addIngestionToNewExperience,
                experienceIdToAddTo = experienceIdToAddTo,
                latestExperienceId = latestExperienceId,
                navigateToExperiencePopUpToExperienceScreen = navigateToExperiencePopUpToExperienceScreen,
                navigateToExperiencePopUpToSubstanceScreen = navigateToExperiencePopUpToSubstanceScreen
            )
        }
    }
}

@Composable
fun AddIngestionButtons(
    addIngestionToExperience: (Int) -> Unit,
    addIngestionToNewExperience: () -> Unit,
    experienceIdToAddTo: Int?,
    latestExperienceId: Int?,
    navigateToExperiencePopUpToSubstanceScreen: (Int) -> Unit,
    navigateToExperiencePopUpToExperienceScreen: (Int) -> Unit,
) {
    val buttonTextStyle = MaterialTheme.typography.h5
    val context = LocalContext.current
    if (experienceIdToAddTo != null) {
        Button(
            onClick = {
                addIngestionToExperience(experienceIdToAddTo)
                Toast.makeText(
                    context, "Ingestion Added",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToExperiencePopUpToExperienceScreen(experienceIdToAddTo)
            }
        ) {
            Text(
                text = "Add Ingestion",
                style = buttonTextStyle,
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
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
                        navigateToExperiencePopUpToSubstanceScreen(latestExperienceId)
                    }
                ) {
                    Text(
                        text = "Add To Latest Experience",
                        style = buttonTextStyle,
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Button(onClick = addIngestionToNewExperience) {
                Text(
                    text = "Add To New Experience",
                    style = buttonTextStyle,
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun TimePickerButton(
    hour: Int,
    minute: Int,
    onSubmitTime: (Int, Int) -> Unit,
    timeString: String
) {
    val context = LocalContext.current
    val timePickerDialog = TimePickerDialog(
        context,
        { _, newHour: Int, newMinute: Int ->
            onSubmitTime(newHour, newMinute)
        }, hour, minute, true
    )
    Button(onClick = timePickerDialog::show) {
        Text(timeString, style = MaterialTheme.typography.h2)
    }
}