package com.example.healthassistant.di

import android.content.Context
import androidx.room.Room
import com.example.healthassistant.data.room.AppDatabase
import com.example.healthassistant.data.room.experiences.ExperienceDao
import com.example.healthassistant.data.room.filter.FilterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
    fun provideFilterDao(appDatabase: AppDatabase): FilterDao =
        appDatabase.filterDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "experiences_db"
        ).build()
}