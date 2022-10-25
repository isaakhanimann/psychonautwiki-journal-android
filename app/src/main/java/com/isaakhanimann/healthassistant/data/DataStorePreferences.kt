package com.isaakhanimann.healthassistant.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.isaakhanimann.healthassistant.ui.utils.getInstant
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

val initialDate: Instant = getInstant(2022, 6, 18, 14, 0)!!

@Singleton
class DataStorePreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//    private val dateKey = longPreferencesKey("substancesDate")
//
//    val instantFlow: Flow<Instant> = context.dataStore.data
//        .map { preferences ->
//            val secSince = preferences[dateKey]
//            if (secSince!=null) {
//                Instant.ofEpochSecond(secSince)
//            } else {
//                initialDate
//            }
//        }
//
//    suspend fun resetDate() {
//        context.dataStore.edit { preferences ->
//            preferences[dateKey] = initialDate.epochSecond
//        }
//    }
//
//    suspend fun saveDate(date: Date) {
//        context.dataStore.edit { preferences ->
//            preferences[dateKey] = date.time
//        }
//    }

}