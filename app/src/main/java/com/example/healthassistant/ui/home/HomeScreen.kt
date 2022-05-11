package com.example.healthassistant.ui.home

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = hiltViewModel()) {
    val experiences = homeViewModel.experiences.collectAsState().value
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Experiences")
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { homeViewModel.isShowingDialog = true }
            ) {
                Icon(Icons.Default.Add, "Add New Experience")
            }
        }
    ) {
        if (homeViewModel.isShowingDialog) {
            AddExperienceDialog(homeViewModel = homeViewModel)
        }
        LazyColumn {
            items(experiences) { experience ->
                Text(text = experience.title)
            }
        }
    }
}

@Composable
fun AddExperienceDialog(homeViewModel: HomeViewModel) {
    AlertDialog(
        onDismissRequest = {
            homeViewModel.isShowingDialog = false
        },
        title = {
            Text(text = "Add Experience")
        },
        text = {
            val focusManager = LocalFocusManager.current
            TextField(
                value = homeViewModel.enteredTitle,
                onValueChange = {
                    homeViewModel.enteredTitle = it
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                maxLines = 1,
                label = { Text(text = "Enter Title") },
                isError = !homeViewModel.isEnteredTitleOk,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

        },
        confirmButton = {
            if (homeViewModel.isEnteredTitleOk) {
                val context = LocalContext.current
                TextButton(
                    onClick = {
                        homeViewModel.dialogConfirmTapped(
                            onSuccess = {
                                Toast.makeText(
                                    context, "Experience Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                ) {
                    Text("Confirm")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    homeViewModel.isShowingDialog = false
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}