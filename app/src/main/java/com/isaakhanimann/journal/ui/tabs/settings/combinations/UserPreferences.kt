/*
 * Copyright (c) 2023. Isaak Hanimann.
 * This file is part of PsychonautWiki Journal.
 *
 * PsychonautWiki Journal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * PsychonautWiki Journal is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PsychonautWiki Journal.  If not, see https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.tabs.settings.combinations

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.SavedTimeDisplayOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val KEY_TIME_DISPLAY_OPTION = stringPreferencesKey("key_time_display_option")
    }

    suspend fun saveTimeDisplayOption(value: SavedTimeDisplayOption) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_TIME_DISPLAY_OPTION] = value.name
        }
    }

    val savedTimeDisplayOptionFlow: Flow<SavedTimeDisplayOption> = dataStore.data
        .map { preferences ->
            val name = preferences[PreferencesKeys.KEY_TIME_DISPLAY_OPTION] ?: SavedTimeDisplayOption.REGULAR.name
            SavedTimeDisplayOption.valueOf(name)
        }
}

