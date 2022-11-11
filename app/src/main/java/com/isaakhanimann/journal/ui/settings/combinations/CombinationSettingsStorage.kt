/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.ui.settings.combinations

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