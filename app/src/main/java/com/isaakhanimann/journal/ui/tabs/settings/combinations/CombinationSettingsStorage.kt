/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CombinationSettingsStorage @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val substanceInteractionKey = stringSetPreferencesKey("substanceInteractions")

    val enabledInteractionsFlow: Flow<Set<String>> = dataStore.data
        .map { preferences ->
            preferences[substanceInteractionKey] ?: emptySet()
        }

    suspend fun toggleSubstanceInteraction(substanceInteraction: String) {
        dataStore.edit { settings ->
            val oldSet = settings[substanceInteractionKey] ?: emptySet()
            val newSet =
                if (oldSet.contains(substanceInteraction)) oldSet.minus(substanceInteraction) else oldSet.plus(
                    substanceInteraction
                )
            settings[substanceInteractionKey] = newSet
        }
    }

    private val substanceInteractionOptions = listOf(
        "Alcohol",
        "Caffeine",
        "Cannabis",
        "Grapefruit",
        "Hormonal birth control",
        "Nicotine",
        "Lithium",
        "MAOI",
        "SSRIs",
        "SNRIs",
        "5-Hydroxytryptophan",
        "Tricyclic antidepressants",
        "Antibiotics",
        "Antihistamine"
    )

    val optionFlow: Flow<List<Option>> = enabledInteractionsFlow.map { set ->
        substanceInteractionOptions.map { name ->
            Option(name = name, enabled = set.contains(name))
        }
    }
}

data class Option(
    val name: String,
    val enabled: Boolean
)