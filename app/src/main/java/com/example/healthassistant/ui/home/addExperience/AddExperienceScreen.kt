package com.example.healthassistant.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.healthassistant.ui.home.addExperience.AddExperienceViewModel
import com.example.healthassistant.ui.home.experience.addingestion.time.DatePickerButton
import com.example.healthassistant.ui.main.routers.navigateToExperienceFromAddExperience
import com.example.healthassistant.ui.search.substance.NavigateBackIcon

@Composable
fun AddExperienceScreen(
    navController: NavController,
    viewModel: AddExperienceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    AddExperienceContent(
        onBackTap = navController::popBackStack,
        day = viewModel.day,
        month = viewModel.month,
        year = viewModel.year,
        onSubmitDate = viewModel::onSubmitDate,
        enteredTitle = viewModel.enteredTitle,
        onChangeOfEnteredTitle = { viewModel.enteredTitle = it },
        isEnteredTitleOk = viewModel.isEnteredTitleOk,
        onConfirmTap = {
            viewModel.onConfirmTap {
                Toast.makeText(
                    context, "Experience Added",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigateToExperienceFromAddExperience(experienceId = it)
            }
        },
        dateString = viewModel.dateString
    )
}

@Preview
@Composable
fun AddExperienceContentPreview() {
    AddExperienceContent(
        onBackTap = {},
        day = 5,
        month = 3,
        year = 2022,
        onSubmitDate = { _: Int, _: Int, _: Int -> },
        enteredTitle = "Day at the Lake",
        onChangeOfEnteredTitle = { },
        isEnteredTitleOk = true,
        onConfirmTap = {},
        dateString = "Wed 5 Jul 2022"
    )
}

@Composable
fun AddExperienceContent(
    onBackTap: () -> Unit,
    day: Int,
    month: Int,
    year: Int,
    onSubmitDate: (Int, Int, Int) -> Unit,
    enteredTitle: String,
    onChangeOfEnteredTitle: (String) -> Unit,
    isEnteredTitleOk: Boolean,
    onConfirmTap: () -> Unit,
    dateString: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Experience") },
                navigationIcon = { NavigateBackIcon(navigateBack = onBackTap) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            DatePickerButton(
                day = day,
                month = month,
                year = year,
                onSubmitDate = onSubmitDate,
                dateString = dateString
            )
            TextField(
                value = enteredTitle,
                onValueChange = onChangeOfEnteredTitle,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.h4,
                maxLines = 1,
                label = { Text(text = "Enter Title", style = MaterialTheme.typography.subtitle1) },
                isError = !isEnteredTitleOk,
                keyboardActions = KeyboardActions(onDone = { onConfirmTap() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Button(
                onClick = onConfirmTap,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = isEnteredTitleOk
            ) {
                Text("Create Experience", style = MaterialTheme.typography.h4)
            }
        }
    }
}