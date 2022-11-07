package com.isaakhanimann.journal.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

val SKIP_INTERACTIONS = booleanPreferencesKey("skipInteractions")

@HiltViewModel
class CombinationSettingsViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    val skipFlow: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[SKIP_INTERACTIONS] ?: false
        }.stateIn(
            initialValue = false,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun toggle() {
        viewModelScope.launch {
            dataStore.edit { settings ->
                val oldValue = settings[SKIP_INTERACTIONS] ?: false
                settings[SKIP_INTERACTIONS] = oldValue.not()
            }
        }
    }
}