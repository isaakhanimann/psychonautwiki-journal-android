package com.isaakhanimann.healthassistant.ui.journal.experience.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.isaakhanimann.healthassistant.ui.utils.JournalTopAppBar


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

@OptIn(ExperimentalMaterial3Api::class)
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
            JournalTopAppBar(title = "Edit Experience")
        },
        floatingActionButton = {
            if (isEnteredTitleOk) {
                ExtendedFloatingActionButton(
                    onClick = onDoneTap,
                    icon = {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done Icon"
                        )
                    },
                    text = { Text("Done") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val focusManager = LocalFocusManager.current
            OutlinedTextField(
                value = enteredTitle,
                onValueChange = onChangeOfEnteredTitle,
                singleLine = true,
                label = { Text(text = "Title") },
                isError = !isEnteredTitleOk,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text(text = "Notes") },
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done, capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }
    }
}