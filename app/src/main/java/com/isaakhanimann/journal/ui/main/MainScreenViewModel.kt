/*
 * Copyright (c) 2022. Free Software Foundation, Inc.
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

package com.isaakhanimann.journal.ui.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

val ARE_CONDITIONS_ACCEPTED = booleanPreferencesKey("are_conditions_accepted")

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    experienceRepo: ExperienceRepository
) : ViewModel() {

    val isAcceptedFlow: StateFlow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ARE_CONDITIONS_ACCEPTED] ?: false
        }.stateIn(
            initialValue = true,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000)
        )

    fun accept() {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[ARE_CONDITIONS_ACCEPTED] = true
            }
        }
    }
}