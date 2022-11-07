package com.isaakhanimann.journal.ui.settings.combinations

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BooleanInteraction(
    val dataStore: DataStore<Preferences>,
    val name: String
) {
    private val key = booleanPreferencesKey(name)

    val flow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[key] ?: false
        }

    suspend fun toggle() {
        dataStore.edit { settings ->
            val oldValue = settings[key] ?: false
            settings[key] = oldValue.not()
        }
    }
}