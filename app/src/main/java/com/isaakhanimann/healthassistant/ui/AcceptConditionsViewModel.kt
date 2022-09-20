package com.isaakhanimann.healthassistant.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val ARE_CONDITIONS_ACCEPTED = booleanPreferencesKey("are_conditions_accepted")

@HiltViewModel
class AcceptConditionsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    val isAcceptedFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ARE_CONDITIONS_ACCEPTED] ?: false
        }

    fun toggleAccepted() {
        viewModelScope.launch {
            dataStore.edit { settings ->
                val currentValue = settings[ARE_CONDITIONS_ACCEPTED] ?: false
                settings[ARE_CONDITIONS_ACCEPTED] = currentValue.not()
            }
        }
    }
}