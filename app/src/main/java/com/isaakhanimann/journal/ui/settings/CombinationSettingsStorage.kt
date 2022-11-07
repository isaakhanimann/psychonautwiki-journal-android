package com.isaakhanimann.journal.ui.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CombinationSettingsStorage @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val skipInteractionsKey = booleanPreferencesKey("skipInteractions")

    val skipFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[skipInteractionsKey] ?: false
        }

    suspend fun toggle() {
        dataStore.edit { settings ->
            val oldValue = settings[skipInteractionsKey] ?: false
            settings[skipInteractionsKey] = oldValue.not()
        }
    }
}