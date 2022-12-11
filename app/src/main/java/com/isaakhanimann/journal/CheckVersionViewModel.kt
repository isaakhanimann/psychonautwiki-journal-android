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

package com.isaakhanimann.journal

import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.net.URL
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

val LAST_TIME_VERSION_CHECKED = longPreferencesKey("last_time_version_checked")

@HiltViewModel
class CheckVersionViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {
    val newerVersionText = mutableStateOf("")
    val isShowingNewerVersionAlert = mutableStateOf(false)

    fun checkVersion() {
        viewModelScope.launch(context = Dispatchers.IO) {
            delay(1000)
            val daysSince =
                Duration.between(lastTimeVersionCheckedFlow.first(), Instant.now()).toDays()
            if (daysSince > 20) {
                val url = URL("https://isaakhanimann.github.io/version.txt")
                try {
                    val onlineVersion = url.readText()
                    val majorVersionOnline = onlineVersion.split(".")[0]
                    val majorVersionApp = BuildConfig.VERSION_NAME.split(".")[0]
                    if (majorVersionOnline != majorVersionApp) {
                        newerVersionText.value =
                            "Your version is ${BuildConfig.VERSION_NAME} but the newest version is $onlineVersion. Visit the website to download the newest version. Your data will be automatically migrated during installation."
                        isShowingNewerVersionAlert.value = true
                    }
                } catch (_: Exception) {
                }
                updateVersionCheck()
            }
        }
    }

    private val lastTimeVersionCheckedFlow: Flow<Instant> = dataStore.data
        .map { preferences ->
            Instant.ofEpochSecond(preferences[LAST_TIME_VERSION_CHECKED] ?: 0)
        }

    private fun updateVersionCheck() {
        viewModelScope.launch {
            dataStore.edit { settings ->
                settings[LAST_TIME_VERSION_CHECKED] = Instant.now().epochSecond
            }
        }
    }
}