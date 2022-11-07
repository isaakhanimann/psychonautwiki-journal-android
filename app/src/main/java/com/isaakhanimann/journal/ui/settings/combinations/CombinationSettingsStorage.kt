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