/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isaakhanimann.journal.data.room.experiences.ExperienceRepository
import com.isaakhanimann.journal.ui.addingestion.time.hourLimitToSeparateIngestions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
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

    val currentExperienceFlow =
        experienceRepo.getSortedExperiencesWithIngestionsAndCompanionsFlow(limit = 3)
            .map { experiences ->
                return@map experiences.firstOrNull { experience ->
                    experience.ingestionsWithCompanions.any {
                        it.ingestion.time > Instant.now().minus(
                            hourLimitToSeparateIngestions, ChronoUnit.HOURS
                        )
                    }
                }
            }
            .stateIn(
                initialValue = null,
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000)
            )
}