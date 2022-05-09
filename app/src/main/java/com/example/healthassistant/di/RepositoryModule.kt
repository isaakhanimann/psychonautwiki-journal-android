package com.example.healthassistant.di

import com.example.healthassistant.data.substances.JSONParser
import com.example.healthassistant.data.substances.SubstanceParser
import com.example.healthassistant.data.substances.Substance
import com.example.healthassistant.repository.SubstanceRepository
import com.example.healthassistant.repository.SubstanceRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSubstanceParser(
        substanceParser: SubstanceParser
    ): JSONParser<Substance>

    @Binds
    @Singleton
    abstract fun bindSubstanceRepository(
        substanceRepositoryImpl: SubstanceRepositoryImpl
    ): SubstanceRepository
}