package com.example.healthassistant.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    val counter = dataStorePreferences.exampleCounterFlow

    fun onIncrementTap() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreferences.incrementCounter()
        }
    }
}