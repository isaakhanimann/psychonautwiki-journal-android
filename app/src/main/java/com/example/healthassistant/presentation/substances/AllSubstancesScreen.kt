package com.example.healthassistant.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthassistant.presentation.substances.AllSubstancesScreenViewModel

@Composable
fun AllSubstancesScreen(viewModel: AllSubstancesScreenViewModel) {
    val state = viewModel.state
    state.error?.let {
        Text(text = it)
    }
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(state.substances) { sub ->
            Text(sub.name)
        }
    }

}