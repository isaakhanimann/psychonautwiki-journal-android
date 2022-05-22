package com.example.healthassistant.ui.home.experience.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.healthassistant.ui.home.experience.addingestion.time.DatePickerButton


@Composable
fun EditExperienceScreen(
    navigateBack: () -> Unit,
    viewModel: EditExperienceViewModel = hiltViewModel()
) {
    EditExperienceScreenContent(
        day = viewModel.day,
        month = viewModel.month,
        year = viewModel.year,
        onSubmitDate = viewModel::onSubmitDate,
        enteredTitle = viewModel.enteredTitle,
        onChangeOfEnteredTitle = { viewModel.enteredTitle = it },
        isEnteredTitleOk = viewModel.isEnteredTitleOk,
        onDoneTap = {
            viewModel.onDoneTap()
            navigateBack()
        },
        dateString = viewModel.dateString,
        text = viewModel.enteredText,
        onTextChange = { viewModel.enteredText = it }
    )
}

@Preview
@Composable
fun EditExperienceScreenPreview() {
    EditExperienceScreenContent(
        day = 5,
        month = 3,
        year = 2022,
        onSubmitDate = { _: Int, _: Int, _: Int -> },
        enteredTitle = "Day at the Lake",
        onChangeOfEnteredTitle = { },
        isEnteredTitleOk = true,
        onDoneTap = {},
        dateString = "Wed 5 Jul 2022",
        text = "Here are some sample notes",
        onTextChange = {}
    )
}

@Composable
fun EditExperienceScreenContent(
    day: Int,
    month: Int,
    year: Int,
    onSubmitDate: (Int, Int, Int) -> Unit,
    enteredTitle: String,
    onChangeOfEnteredTitle: (String) -> Unit,
    isEnteredTitleOk: Boolean,
    onDoneTap: () -> Unit,
    dateString: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Edit Experience") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
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
            TextField(
                value = enteredTitle,
                onValueChange = onChangeOfEnteredTitle,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.h4,
                maxLines = 1,
                label = { Text(text = "Title", style = MaterialTheme.typography.subtitle1) },
                isError = !isEnteredTitleOk,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            TextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text(text = "Notes", style = MaterialTheme.typography.subtitle1) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )
            Button(
                onClick = onDoneTap,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = isEnteredTitleOk
            ) {
                Text("Save Changes", style = MaterialTheme.typography.h4)
            }
        }
    }

}