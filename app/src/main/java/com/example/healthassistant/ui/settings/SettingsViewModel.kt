package com.example.healthassistant.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthassistant.data.DataStorePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStorePreferences: DataStorePreferences
) : ViewModel() {

    val dateFlow = dataStorePreferences.dateFlow

    fun updateDateToNow() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorePreferences.saveDate(date = Date())
        }
    }
}