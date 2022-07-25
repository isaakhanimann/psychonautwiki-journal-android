package com.isaakhanimann.healthassistant.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

val initialDate: Date get() {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(2022, 6, 18, 14, 0)
    return calendar.time
}

@Singleton
class DataStorePreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dateKey = longPreferencesKey("substancesDate")

    val dateFlow: Flow<Date> = context.dataStore.data
        .map { preferences ->
            val secSince = preferences[dateKey]
            if (secSince!=null) {
                Date(secSince)
            } else {
                initialDate
            }
        }

    suspend fun resetDate() {
        context.dataStore.edit { preferences ->
            preferences[dateKey] = initialDate.time
        }
    }

    suspend fun saveDate(date: Date) {
        context.dataStore.edit { preferences ->
            preferences[dateKey] = date.time
        }
    }

}