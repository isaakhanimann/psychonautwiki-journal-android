/*
 * Copyright (c) 2022. Isaak Hanimann.
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

package com.isaakhanimann.journal.ui.tabs.safer.settings.combinations

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CombinationSettingsStorage @Inject constructor(dataStore: DataStore<Preferences>) {

    val skipInteractor = BooleanInteraction(dataStore, "skipInteractions")

    val substanceInteractors = listOf(
        BooleanInteraction(dataStore, "Alcohol"),
        BooleanInteraction(dataStore, "Caffeine"),
        BooleanInteraction(dataStore, "Cannabis"),
        BooleanInteraction(dataStore, "Grapefruit"),
        BooleanInteraction(dataStore, "Hormonal birth control")
    )
}