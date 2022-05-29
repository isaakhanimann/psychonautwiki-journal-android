package com.example.healthassistant.ui.home.addExperience

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.ui.home.experience.addingestion.time.DatePickerButton

@Composable
fun AddExperienceScreen(
    navigateToExperienceFromAddExperience: (experienceId: Int) -> Unit,
    viewModel: AddExperienceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    AddExperienceContent(
        day = viewModel.day,
        month = viewModel.month,
        year = viewModel.year,
        onSubmitDate = viewModel::onSubmitDate,
        title = viewModel.title,
        onChangeOfTitle = { viewModel.title = it },
        isTitleOk = viewModel.isTitleOk,
        onConfirmTap = {
            viewModel.onConfirmTap {
                Toast.makeText(
                    context, "Experience Added",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToExperienceFromAddExperience(it)
            }
        },
        dateString = viewModel.dateString,
        notes = viewModel.notes,
        onNotesChange = {
            viewModel.notes = it
        }
    )
}

@Preview
@Composable
fun AddExperienceContentPreview() {
    AddExperienceContent(
        day = 5,
        month = 3,
        year = 2022,
        onSubmitDate = { _: Int, _: Int, _: Int -> },
        title = "Day at the Lake",
        onChangeOfTitle = { },
        isTitleOk = true,
        onConfirmTap = {},
        dateString = "Wed 5 Jul 2022",
        notes = "",
        onNotesChange = {}
    )
}

@Composable
fun AddExperienceContent(
    day: Int,
    month: Int,
    year: Int,
    onSubmitDate: (Int, Int, Int) -> Unit,
    title: String,
    onChangeOfTitle: (String) -> Unit,
    isTitleOk: Boolean,
    onConfirmTap: () -> Unit,
    dateString: String,
    notes: String,
    onNotesChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Experience") },
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
            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                value = title,
                onValueChange = onChangeOfTitle,
                textStyle = MaterialTheme.typography.h5,
                maxLines = 2,
                label = { Text(text = "Title", style = MaterialTheme.typography.subtitle1) },
                isError = !isTitleOk,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = notes,
                onValueChange = onNotesChange,
                label = { Text(text = "Notes", style = MaterialTheme.typography.subtitle1) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            Button(
                onClick = onConfirmTap,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = isTitleOk
            ) {
                Text("Create Experience")
            }
        }
    }
}