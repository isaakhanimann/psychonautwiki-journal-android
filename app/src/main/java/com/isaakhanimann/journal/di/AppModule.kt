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

package com.isaakhanimann.journal.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.isaakhanimann.journal.data.room.AppDatabase
import com.isaakhanimann.journal.data.gamification.GamificationService
import com.isaakhanimann.journal.data.gamification.GamificationServiceImpl
import com.isaakhanimann.journal.data.gamification.PersonalizedInsightService
import com.isaakhanimann.journal.data.gamification.PersonalizedInsightServiceImpl
import com.isaakhanimann.journal.data.gamification.WeeklyChallengeService
import com.isaakhanimann.journal.data.gamification.InstantSerializer
import com.isaakhanimann.journal.data.gamification.WeeklyChallengeServiceImpl
import com.isaakhanimann.journal.data.room.experiences.ExperienceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideExperiencesDao(appDatabase: AppDatabase): ExperienceDao =
        appDatabase.experienceDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "experiences_db"
        ).build()

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { appContext.preferencesDataStoreFile("user_preferences") }
        )
    }

    @Singleton
    @Provides
    fun provideGamificationService(experienceDao: ExperienceDao): GamificationService = GamificationServiceImpl(experienceDao)

    @Singleton
    @Provides
    fun provideWeeklyChallengeService(): WeeklyChallengeService = WeeklyChallengeServiceImpl()

    @Singleton
    @Provides
    fun providePersonalizedInsightService(experienceDao: ExperienceDao): PersonalizedInsightService = PersonalizedInsightServiceImpl(experienceDao)

    @Singleton
    @Provides
    fun provideJson(): Json {
        return Json {
            serializersModule = SerializersModule {
                contextual(java.time.Instant::class, InstantSerializer)
            }
        }
    }
}