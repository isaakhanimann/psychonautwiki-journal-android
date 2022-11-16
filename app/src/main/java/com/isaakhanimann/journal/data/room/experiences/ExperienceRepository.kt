/*
 * Copyright (c) 2022.
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 3.0. If a copy of the MPL was not distributed with this file, You can obtain one at https://www.gnu.org/licenses/gpl-3.0.en.html.
 */

package com.isaakhanimann.journal.data.room.experiences

import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestions
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithExperience
import com.isaakhanimann.journal.ui.tabs.safer.settings.JournalExport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExperienceRepository @Inject constructor(private val experienceDao: ExperienceDao) {
    suspend fun insert(experience: Experience) = experienceDao.insert(experience)
    suspend fun insert(ingestion: Ingestion) = experienceDao.insert(ingestion)
    suspend fun update(experience: Experience) = experienceDao.update(experience)
    suspend fun update(ingestion: Ingestion) = experienceDao.update(ingestion)
    suspend fun insertIngestionExperienceAndCompanion(
        ingestion: Ingestion,
        experience: Experience,
        substanceCompanion: SubstanceCompanion
    ) = experienceDao.insertIngestionExperienceAndCompanion(
        ingestion,
        experience,
        substanceCompanion
    )

    suspend fun insertEverything(
        journalExport: JournalExport
    ) = experienceDao.insertEverything(journalExport)

    suspend fun insertIngestionAndCompanion(
        ingestion: Ingestion,
        substanceCompanion: SubstanceCompanion
    ) = experienceDao.insertIngestionAndCompanion(
        ingestion,
        substanceCompanion
    )

    suspend fun deleteEverything() = experienceDao.deleteEverything()

    suspend fun delete(ingestion: Ingestion) = experienceDao.deleteIngestion(ingestion)

    suspend fun delete(experience: Experience) =
        experienceDao.deleteExperience(experience)

    suspend fun delete(experienceWithIngestions: ExperienceWithIngestions) =
        experienceDao.deleteExperienceWithIngestions(experienceWithIngestions)

    suspend fun delete(experienceWithIngestionsAndCompanions: ExperienceWithIngestionsAndCompanions) =
        experienceDao.deleteExperienceWithIngestions(experienceWithIngestionsAndCompanions)

    fun getSortedExperiencesWithIngestionsAndCompanionsFlow(): Flow<List<ExperienceWithIngestionsAndCompanions>> =
        experienceDao.getSortedExperiencesWithIngestionsAndCompanionsFlow()
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getSortedExperiencesWithIngestionsAndCompanionsFlow(limit: Int): Flow<List<ExperienceWithIngestionsAndCompanions>> =
        experienceDao.getSortedExperiencesWithIngestionsAndCompanionsFlow(limit)
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getSortedExperiencesWithIngestionsFlow(): Flow<List<ExperienceWithIngestions>> =
        experienceDao.getSortedExperiencesWithIngestionsFlow()
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getCustomSubstancesFlow(): Flow<List<CustomSubstance>> =
        experienceDao.getCustomSubstancesFlow()
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getCustomSubstanceFlow(name: String): Flow<CustomSubstance?> =
        experienceDao.getCustomSubstanceFlow(name)
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getIngestionsWithExperiencesFlow(
        fromInstant: Instant,
        toInstant: Instant
    ): Flow<List<IngestionWithExperience>> =
        experienceDao.getIngestionWithExperiencesFlow(fromInstant, toInstant)
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getLastUsedSubstanceNamesFlow(limit: Int): Flow<List<String>> =
        experienceDao.getLastUsedSubstanceNamesFlow(limit).flowOn(Dispatchers.IO).conflate()

    suspend fun getExperience(id: Int): Experience? = experienceDao.getExperience(id)
    fun getIngestionFlow(id: Int) = experienceDao.getIngestionFlow(id)
    fun getIngestionWithExperienceFlow(id: Int) = experienceDao.getIngestionWithExperienceFlow(id)
    fun getIngestionWithCompanionFlow(id: Int) = experienceDao.getIngestionWithCompanionFlow(id)
    fun getExperienceWithIngestionsAndCompanionsFlow(experienceId: Int) =
        experienceDao.getExperienceWithIngestionsAndCompanionsFlow(experienceId)
            .flowOn(Dispatchers.IO)
            .conflate()

    suspend fun getLatestIngestionOfEverySubstanceSinceDate(instant: Instant): List<Ingestion> =
        experienceDao.getLatestIngestionOfEverySubstanceSinceDate(instant)

    suspend fun getIngestionsSinceDate(instant: Instant): List<Ingestion> =
        experienceDao.getIngestionsSinceDate(instant)

    suspend fun getAllIngestions(): List<Ingestion> =
        experienceDao.getAllIngestions()

    suspend fun getAllExperiences(): List<Experience> =
        experienceDao.getAllExperiences()

    suspend fun getAllCustomSubstances(): List<CustomSubstance> =
        experienceDao.getAllCustomSubstances()

    suspend fun getAllSubstanceCompanions(): List<SubstanceCompanion> =
        experienceDao.getAllSubstanceCompanions()

    suspend fun insert(substanceCompanion: SubstanceCompanion) =
        experienceDao.insert(substanceCompanion)

    suspend fun delete(substanceCompanion: SubstanceCompanion) =
        experienceDao.delete(substanceCompanion)

    suspend fun update(substanceCompanion: SubstanceCompanion) =
        experienceDao.update(substanceCompanion)

    suspend fun insert(customSubstance: CustomSubstance) =
        experienceDao.insert(customSubstance)

    suspend fun delete(customSubstance: CustomSubstance) =
        experienceDao.delete(customSubstance)

    suspend fun update(customSubstance: CustomSubstance) =
        experienceDao.update(customSubstance)

    fun getSortedIngestionsWithSubstanceCompanionsFlow() =
        experienceDao.getSortedIngestionsWithSubstanceCompanionsFlow()
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getSortedIngestionsWithSubstanceCompanionsFlow(limit: Int) =
        experienceDao.getSortedIngestionsWithSubstanceCompanionsFlow(limit)
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getSortedIngestionsFlow() =
        experienceDao.getSortedIngestionsFlow()
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getSortedIngestionsFlow(substanceName: String, limit: Int) =
        experienceDao.getSortedIngestionsFlow(substanceName, limit)
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getSortedIngestionsFlow(substanceName: String) =
        experienceDao.getSortedIngestionsFlow(substanceName)
            .flowOn(Dispatchers.IO)
            .conflate()

    fun getAllSubstanceCompanionsFlow() = experienceDao.getAllSubstanceCompanionsFlow()
        .flowOn(Dispatchers.IO)
        .conflate()

    fun getSubstanceCompanionFlow(substanceName: String) =
        experienceDao.getSubstanceCompanionFlow(substanceName)
}