package com.example.healthassistant.di

import android.content.Context
import androidx.room.Room
import com.example.healthassistant.data.experiences.ExperienceDao
import com.example.healthassistant.data.experiences.ExperienceDatabase
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
    fun provideExperiencesDao(experienceDatabase: ExperienceDatabase): ExperienceDao =
        experienceDatabase.experienceDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): ExperienceDatabase =
        Room.databaseBuilder(
            context,
            ExperienceDatabase::class.java,
            "experiences_db"
        ).build()
}