package com.example.healthassistant.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.substances.PsychonautWikiAPIImplementation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    psychonautWikiAPIImplementation: PsychonautWikiAPIImplementation
) : ViewModel() {

    val text = mutableStateOf("")

    init {
        viewModelScope.launch(Dispatchers.IO) {
            text.value = psychonautWikiAPIImplementation.getStringFromAPI()
        }
    }
}