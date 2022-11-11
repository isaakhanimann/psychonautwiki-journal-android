/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

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