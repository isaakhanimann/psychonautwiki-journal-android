package com.isaakhanimann.healthassistant.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    val dataStore: DataStore<Preferences>
)

val EXAMPLE_COUNTER = intPreferencesKey("example_counter")

@HiltViewModel
class AcceptConditionsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val exampleCounterFlow: Flow<Int> = userPreferencesRepository.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[EXAMPLE_COUNTER] ?: 0
        }

    fun incrementCounter() {
        viewModelScope.launch {
            userPreferencesRepository.dataStore.edit { settings ->
                val currentCounterValue = settings[EXAMPLE_COUNTER] ?: 0
                settings[EXAMPLE_COUNTER] = currentCounterValue + 1
            }
        }
    }
}