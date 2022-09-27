package com.isaakhanimann.healthassistant.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.healthassistant.data.DataStorePreferences
import com.isaakhanimann.healthassistant.data.substances.repositories.SubstanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

val Instant.asTextWithDateAndTime: String get() {
    val dateTime = LocalDateTime.ofInstant(this, ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    return dateTime.format(formatter)
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    dataStorePreferences: DataStorePreferences,
    private val substanceRepository: SubstanceRepository
) : ViewModel() {

    val dateStringFlow = dataStorePreferences.instantFlow.mapNotNull { it.asTextWithDateAndTime }

    var isUpdating by mutableStateOf(false)

    fun updateSubstances(onSuccess: () -> Unit, onError: () -> Unit) {
        isUpdating = true
        viewModelScope.launch(Dispatchers.IO) {
            val isSuccess = substanceRepository.update()
            withContext(Dispatchers.Main) {
                if (isSuccess) {
                    onSuccess()
                } else {
                    onError()
                }
                isUpdating = false
            }
        }
    }

    fun resetSubstances() {
        isUpdating = true
        viewModelScope.launch(Dispatchers.IO) {
            substanceRepository.reset()
            withContext(Dispatchers.Main) {
                isUpdating = false
            }
        }
    }
}