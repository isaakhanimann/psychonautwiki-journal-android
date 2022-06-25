package com.example.healthassistant.ui.experiences.experience.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun EditExperienceScreen(
    navigateBack: () -> Unit,
    viewModel: EditExperienceViewModel = hiltViewModel()
) {
    EditExperienceScreen(
        enteredTitle = viewModel.enteredTitle,
        onChangeOfEnteredTitle = { viewModel.enteredTitle = it },
        isEnteredTitleOk = viewModel.isEnteredTitleOk,
        onDoneTap = {
            viewModel.onDoneTap()
            navigateBack()
        },
        text = viewModel.enteredText,
        onTextChange = { viewModel.enteredText = it }
    )
}

@Preview
@Composable
fun EditExperienceScreenPreview() {
    EditExperienceScreen(
        enteredTitle = "Day at the Lake",
        onChangeOfEnteredTitle = { },
        isEnteredTitleOk = true,
        onDoneTap = {},
        text = "Here are some sample notes",
        onTextChange = {}
    )
}

@Composable
fun EditExperienceScreen(
    enteredTitle: String,
    onChangeOfEnteredTitle: (String) -> Unit,
    isEnteredTitleOk: Boolean,
    onDoneTap: () -> Unit,
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                value = enteredTitle,
                onValueChange = onChangeOfEnteredTitle,
                textStyle = MaterialTheme.typography.h5,
                maxLines = 2,
                label = { Text(text = "Title", style = MaterialTheme.typography.subtitle1) },
                isError = !isEnteredTitleOk,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text(text = "Notes", style = MaterialTheme.typography.subtitle1) },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            Button(
                onClick = onDoneTap,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = isEnteredTitleOk
            ) {
                Text("Save Changes")
            }
        }
    }
}