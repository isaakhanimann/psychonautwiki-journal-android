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
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.isaakhanimann.journal.ui.tabs.journal.experience.components.SavedTimeDisplayOption
import com.isaakhanimann.journal.ui.tabs.settings.combinations.UserPreferences.PreferencesKeys.KEY_LANGUAGE
import com.isaakhanimann.journal.ui.tabs.settings.combinations.UserPreferences.PreferencesKeys.SYSTEM_DEFAULT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKeys {
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_TIME_DISPLAY_OPTION = stringPreferencesKey("key_time_display_option")

        // last ingestion time of experience is used when adding an ingestion from a past experience
        // cloned ingestion time is used to copy the time from another ingestion
        // those values need to be set/reset whenever an ingestion is added
        val KEY_LAST_INGESTION_OF_EXPERIENCE = longPreferencesKey("KEY_LAST_INGESTION_OF_EXPERIENCE")
        val KEY_CLONED_INGESTION_TIME = longPreferencesKey("KEY_CLONED_INGESTION_TIME")

        val KEY_HIDE_ORAL_DISCLAIMER = booleanPreferencesKey("key_hide_oral_disclaimer")
        val KEY_HIDE_DOSAGE_DOTS = booleanPreferencesKey("key_hide_dosage_dots")
        val KEY_ARE_SUBSTANCE_HEIGHTS_INDEPENDENT = booleanPreferencesKey("KEY_ARE_SUBSTANCE_HEIGHTS_INDEPENDENT")
        val KEY_IS_TIMELINE_HIDDEN = booleanPreferencesKey("KEY_IS_TIMELINE_HIDDEN")

        const val SYSTEM_DEFAULT = "SYSTEM"
    }

    suspend fun saveTimeDisplayOption(value: SavedTimeDisplayOption) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_TIME_DISPLAY_OPTION] = value.name
        }
    }

    val savedTimeDisplayOptionFlow: Flow<SavedTimeDisplayOption> = dataStore.data
        .map { preferences ->
            val name = preferences[PreferencesKeys.KEY_TIME_DISPLAY_OPTION] ?: SavedTimeDisplayOption.AUTO.name
            SavedTimeDisplayOption.valueOf(name)
        }

    suspend fun saveLastIngestionTimeOfExperience(value: Instant?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_LAST_INGESTION_OF_EXPERIENCE] = value?.epochSecond ?: 0L
        }
    }

    val lastIngestionTimeOfExperienceFlow: Flow<Instant?> = dataStore.data
        .map { preferences ->
            val epochSecond = preferences[PreferencesKeys.KEY_LAST_INGESTION_OF_EXPERIENCE] ?: 0L
            if (epochSecond != 0L) {
                Instant.ofEpochSecond(epochSecond)
            } else {
                null
            }
        }

    suspend fun saveClonedIngestionTime(value: Instant?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_CLONED_INGESTION_TIME] = value?.epochSecond ?: 0L
        }
    }

    val clonedIngestionTimeFlow: Flow<Instant?> = dataStore.data
        .map { preferences ->
            val epochSecond = preferences[PreferencesKeys.KEY_CLONED_INGESTION_TIME] ?: 0L
            if (epochSecond != 0L) {
                Instant.ofEpochSecond(epochSecond)
            } else {
                null
            }
        }

    suspend fun saveOralDisclaimerIsHidden(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_HIDE_ORAL_DISCLAIMER] = value
        }
    }

    val isOralDisclaimerHiddenFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.KEY_HIDE_ORAL_DISCLAIMER] ?: false
        }

    suspend fun saveDosageDotsAreHidden(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_HIDE_DOSAGE_DOTS] = value
        }
    }

    val areDosageDotsHiddenFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.KEY_HIDE_DOSAGE_DOTS] ?: false
        }

    suspend fun saveAreSubstanceHeightsIndependent(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_ARE_SUBSTANCE_HEIGHTS_INDEPENDENT] = value
        }
    }

    val areSubstanceHeightsIndependentFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.KEY_ARE_SUBSTANCE_HEIGHTS_INDEPENDENT] ?: false
        }

    val isTimelineHiddenFlow: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.KEY_IS_TIMELINE_HIDDEN] ?: false
        }

    suspend fun saveIsTimelineHidden(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_IS_TIMELINE_HIDDEN] = value
        }
    }

    val languageFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[KEY_LANGUAGE] ?: SYSTEM_DEFAULT
        }

    suspend fun setLanguage(language: String) {
        dataStore.edit { settings ->
            settings[KEY_LANGUAGE] = language
        }
    }
}

