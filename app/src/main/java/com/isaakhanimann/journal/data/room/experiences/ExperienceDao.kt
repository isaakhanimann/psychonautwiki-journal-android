/*
 * Copyright (c) 2022-2023. Isaak Hanimann.
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

package com.isaakhanimann.journal.data.room.experiences

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.isaakhanimann.journal.data.room.experiences.entities.CustomSubstance
import com.isaakhanimann.journal.data.room.experiences.entities.CustomUnit
import com.isaakhanimann.journal.data.room.experiences.entities.Experience
import com.isaakhanimann.journal.data.room.experiences.entities.Ingestion
import com.isaakhanimann.journal.data.room.experiences.entities.Location
import com.isaakhanimann.journal.data.room.experiences.entities.ShulginRating
import com.isaakhanimann.journal.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.journal.data.room.experiences.entities.TimedNote
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestions
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsCompanionsAndRatings
import com.isaakhanimann.journal.data.room.experiences.relations.ExperienceWithIngestionsTimedNotesAndRatings
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanion
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithCompanionAndCustomUnit
import com.isaakhanimann.journal.data.room.experiences.relations.IngestionWithExperienceAndCustomUnit
import com.isaakhanimann.journal.ui.tabs.settings.JournalExport
import kotlinx.coroutines.flow.Flow
import java.time.Instant

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience ORDER BY creationDate DESC")
    fun getSortedExperiencesFlow(): Flow<List<Experience>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getIngestionsSortedDescendingFlow(): Flow<List<Ingestion>>

    @Query(
        "SELECT * FROM ingestion as i" +
                " INNER JOIN (SELECT id, MAX(time) AS maxTime FROM ingestion WHERE time > :instant GROUP BY substanceName) as sub" +
                " ON i.id = sub.id AND i.time = sub.maxTime" +
                " ORDER BY time DESC"
    )
    suspend fun getLatestIngestionOfEverySubstanceSinceDate(instant: Instant): List<Ingestion>

    @Query("SELECT * FROM ingestion WHERE time > :instant")
    suspend fun getIngestionsSinceDate(instant: Instant): List<Ingestion>

    @Query("SELECT * FROM ingestion")
    suspend fun getAllIngestions(): List<Ingestion>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate")
    suspend fun getAllExperiencesWithIngestionsSorted(): List<ExperienceWithIngestions>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate")
    suspend fun getAllExperiencesWithIngestionsTimedNotesAndRatingsSorted(): List<ExperienceWithIngestionsTimedNotesAndRatings>

    @Query("SELECT * FROM customunit ORDER BY creationDate")
    suspend fun getAllCustomUnitsSorted(): List<CustomUnit>

    @Query("SELECT * FROM customsubstance")
    suspend fun getAllCustomSubstances(): List<CustomSubstance>

    @Query("SELECT * FROM substancecompanion")
    suspend fun getAllSubstanceCompanions(): List<SubstanceCompanion>

    @Query("SELECT substanceName FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getSortedLastUsedSubstanceNamesFlow(limit: Int): Flow<List<String>>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate DESC")
    fun getSortedExperiencesWithIngestionsAndCompanionsFlow(): Flow<List<ExperienceWithIngestionsAndCompanions>>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate DESC")
    fun getSortedExperienceWithIngestionsCompanionsAndRatingsFlow(): Flow<List<ExperienceWithIngestionsCompanionsAndRatings>>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate DESC LIMIT :limit")
    fun getSortedExperiencesWithIngestionsAndCompanionsFlow(limit: Int): Flow<List<ExperienceWithIngestionsAndCompanions>>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate DESC")
    fun getSortedExperiencesWithIngestionsFlow(): Flow<List<ExperienceWithIngestions>>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY sortDate DESC")
    fun getSortedExperiencesWithIngestionsAndCustomUnitsFlow(): Flow<List<ExperienceWithIngestionsAndCompanions>>

    @Transaction
    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getSortedIngestionsWithSubstanceCompanionsFlow(): Flow<List<IngestionWithCompanionAndCustomUnit>>

    @Transaction
    @Query("SELECT * FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getSortedIngestionsWithSubstanceCompanionsFlow(limit: Int): Flow<List<IngestionWithCompanionAndCustomUnit>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getSortedIngestions(limit: Int): Flow<List<Ingestion>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getSortedIngestionsFlow(): Flow<List<Ingestion>>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT :limit")
    fun getSortedIngestionsFlow(substanceName: String, limit: Int): Flow<List<Ingestion>>

    @Query("SELECT * FROM customsubstance")
    fun getCustomSubstancesFlow(): Flow<List<CustomSubstance>>

    @Query("SELECT * FROM customsubstance WHERE name = :name")
    fun getCustomSubstanceFlow(name: String): Flow<CustomSubstance?>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC")
    fun getSortedIngestionsFlow(substanceName: String): Flow<List<Ingestion>>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC")
    fun getSortedIngestionsWithExperienceAndCustomUnitFlow(substanceName: String): Flow<List<IngestionWithExperienceAndCustomUnit>>

    @Query("SELECT * FROM experience WHERE id =:id")
    suspend fun getExperience(id: Int): Experience?

    @Transaction
    @Query("SELECT * FROM experience WHERE id =:id")
    suspend fun getExperienceWithIngestionsCompanionsAndRatings(id: Int): ExperienceWithIngestionsCompanionsAndRatings?

    @Transaction
    @Query("SELECT * FROM ingestion WHERE experienceId =:experienceId")
    suspend fun getIngestionsWithCompanions(experienceId: Int): List<IngestionWithCompanionAndCustomUnit>

    @Query("SELECT * FROM ingestion WHERE experienceId =:experienceId")
    suspend fun getIngestions(experienceId: Int): List<Ingestion>

    @Query("SELECT * FROM shulginrating WHERE id =:id")
    suspend fun getRating(id: Int): ShulginRating?

    @Query("SELECT * FROM timednote WHERE id =:id")
    suspend fun getTimedNote(id: Int): TimedNote?

    @Query("SELECT * FROM customunit WHERE id =:id")
    suspend fun getCustomUnit(id: Int): CustomUnit?

    @Query("SELECT * FROM experience WHERE id =:id")
    fun getExperienceFlow(id: Int): Flow<Experience?>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE id =:id")
    fun getIngestionWithExperienceFlow(id: Int): Flow<IngestionWithExperienceAndCustomUnit?>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE time > :fromInstant AND time < :toInstant")
    fun getIngestionWithExperiencesFlow(
        fromInstant: Instant,
        toInstant: Instant
    ): Flow<List<IngestionWithExperienceAndCustomUnit>>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE time > :fromInstant AND time < :toInstant")
    suspend fun getIngestionsWithCompanions(
        fromInstant: Instant,
        toInstant: Instant
    ): List<IngestionWithCompanion>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE id =:id")
    fun getIngestionFlow(id: Int): Flow<IngestionWithCompanionAndCustomUnit?>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE id =:id")
    fun getIngestionWithCompanionFlow(id: Int): Flow<IngestionWithCompanionAndCustomUnit?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(experience: Experience)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ingestion: Ingestion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(rating: ShulginRating)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(customUnit: CustomUnit)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(timedNote: TimedNote)

    @Delete
    suspend fun delete(experience: Experience)

    @Delete
    suspend fun delete(rating: ShulginRating)

    @Delete
    suspend fun delete(timedNote: TimedNote)

    @Transaction
    suspend fun deleteExperienceWithIngestions(experienceWithIngestions: ExperienceWithIngestions) {
        delete(experience = experienceWithIngestions.experience)
        experienceWithIngestions.ingestions.forEach {
            delete(it)
        }
    }

    @Delete
    suspend fun delete(ingestion: Ingestion)

    @Delete
    suspend fun delete(customUnit: CustomUnit)

    @Transaction
    suspend fun deleteEverything() {
        deleteAllIngestions()
        deleteAllTimedNotes()
        deleteAllExperiences()
        deleteAllSubstanceCompanions()
        deleteAllCustomSubstances()
        deleteAllRatings()
        deleteAllCustomUnits()
    }

    @Transaction
    suspend fun deleteEverythingOfExperience(experienceId: Int) {
        deleteIngestions(experienceId)
        deleteRatings(experienceId)
        deleteExperience(experienceId)
    }


    @Transaction
    @Query("DELETE FROM ingestion")
    suspend fun deleteAllIngestions()

    @Transaction
    @Query("DELETE FROM timedNote")
    suspend fun deleteAllTimedNotes()

    @Transaction
    @Query("DELETE FROM ingestion WHERE experienceId = :experienceId")
    suspend fun deleteIngestions(experienceId: Int)

    @Transaction
    @Query("DELETE FROM shulginrating WHERE experienceId = :experienceId")
    suspend fun deleteRatings(experienceId: Int)

    @Transaction
    @Query("DELETE FROM experience WHERE id = :experienceId")
    suspend fun deleteExperience(experienceId: Int)

    @Transaction
    @Query("DELETE FROM shulginrating")
    suspend fun deleteAllRatings()

    @Transaction
    @Query("DELETE FROM customunit")
    suspend fun deleteAllCustomUnits()

    @Transaction
    @Query("DELETE FROM experience")
    suspend fun deleteAllExperiences()

    @Transaction
    @Query("DELETE FROM substancecompanion")
    suspend fun deleteAllSubstanceCompanions()

    @Transaction
    @Query("DELETE FROM customsubstance")
    suspend fun deleteAllCustomSubstances()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingestion: Ingestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(rating: ShulginRating)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customUnit: CustomUnit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timedNote: TimedNote)

    @Transaction
    suspend fun insertIngestionExperienceAndCompanion(
        ingestion: Ingestion,
        experience: Experience,
        substanceCompanion: SubstanceCompanion
    ) {
        insert(ingestion)
        insert(experience)
        insert(substanceCompanion)
    }

    @Transaction
    suspend fun insertEverything(
        journalExport: JournalExport
    ) {
        journalExport.experiences.forEachIndexed { indexExperience, experienceSerializable ->
            val experienceID = indexExperience + 1
            val newExperience = Experience(
                id = experienceID,
                title = experienceSerializable.title,
                text = experienceSerializable.text,
                creationDate = experienceSerializable.creationDate,
                sortDate = experienceSerializable.sortDate,
                isFavorite = experienceSerializable.isFavorite,
                location = if (experienceSerializable.location != null) {
                    Location(
                        name = experienceSerializable.location.name,
                        longitude = experienceSerializable.location.longitude,
                        latitude = experienceSerializable.location.latitude
                    )
                } else {
                    null
                }
            )
            insert(newExperience)
            experienceSerializable.ingestions.forEach { ingestionSerializable ->
                val newIngestion = Ingestion(
                    substanceName = ingestionSerializable.substanceName,
                    time = ingestionSerializable.time,
                    creationDate = ingestionSerializable.creationDate,
                    administrationRoute = ingestionSerializable.administrationRoute,
                    dose = ingestionSerializable.dose,
                    isDoseAnEstimate = ingestionSerializable.isDoseAnEstimate,
                    estimatedDoseVariance = ingestionSerializable.estimatedDoseVariance,
                    units = ingestionSerializable.units,
                    experienceId = experienceID,
                    notes = ingestionSerializable.notes,
                    stomachFullness = ingestionSerializable.stomachFullness,
                    consumerName = ingestionSerializable.consumerName,
                    customUnitId = ingestionSerializable.customUnitId
                )
                insert(newIngestion)
            }
            experienceSerializable.timedNotes.forEach { timedNoteSerializable ->
                val newTimedNote = TimedNote(
                    time = timedNoteSerializable.time,
                    creationDate = timedNoteSerializable.creationDate,
                    experienceId = experienceID,
                    isPartOfTimeline = timedNoteSerializable.isPartOfTimeline,
                    color = timedNoteSerializable.color,
                    note = timedNoteSerializable.note
                )
                insert(newTimedNote)
            }
            experienceSerializable.ratings.forEach { ratingSerializable ->
                val newRating = ShulginRating(
                    time = ratingSerializable.time,
                    creationDate = ratingSerializable.creationDate,
                    option = ratingSerializable.option,
                    experienceId = experienceID
                )
                insert(newRating)
            }
        }
        journalExport.substanceCompanions.forEach { insert(it) }
        journalExport.customSubstances.forEach { insert(it) }
        journalExport.customUnits.forEach {
            insert(
                CustomUnit(
                    id = it.id,
                    substanceName = it.substanceName,
                    name = it.name,
                    creationDate = it.creationDate,
                    administrationRoute = it.administrationRoute,
                    dose = it.dose,
                    estimatedDoseVariance = it.estimatedDoseVariance,
                    isEstimate = it.isEstimate,
                    isArchived = it.isArchived,
                    unit = it.unit,
                    originalUnit = it.originalUnit,
                    note = it.note
                )
            )
        }
    }

    @Transaction
    suspend fun insertIngestionAndCompanion(
        ingestion: Ingestion,
        substanceCompanion: SubstanceCompanion
    ) {
        insert(ingestion)
        insert(substanceCompanion)
    }

    @Transaction
    @Query("SELECT * FROM experience WHERE id = :experienceId")
    fun getExperienceWithIngestionsAndCompanionsFlow(experienceId: Int): Flow<ExperienceWithIngestionsAndCompanions?>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE experienceId = :experienceId")
    fun getIngestionsWithCompanionsFlow(experienceId: Int): Flow<List<IngestionWithCompanionAndCustomUnit>>

    @Query("SELECT * FROM shulginrating WHERE experienceId = :experienceId")
    fun getRatingsFlow(experienceId: Int): Flow<List<ShulginRating>>

    @Query("SELECT * FROM timednote WHERE experienceId = :experienceId ORDER BY time")
    fun getTimedNotesFlowSorted(experienceId: Int): Flow<List<TimedNote>>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT 1")
    suspend fun getLastIngestion(substanceName: String): Ingestion?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(substanceCompanion: SubstanceCompanion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customSubstance: CustomSubstance)

    @Delete
    suspend fun delete(customSubstance: CustomSubstance)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(customSubstance: CustomSubstance)

    @Delete
    suspend fun delete(substanceCompanion: SubstanceCompanion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(substanceCompanion: SubstanceCompanion)

    @Query("SELECT * FROM substancecompanion WHERE substanceName =:substanceName")
    fun getSubstanceCompanionFlow(substanceName: String): Flow<SubstanceCompanion?>

    @Query("SELECT * FROM substancecompanion")
    fun getAllSubstanceCompanionsFlow(): Flow<List<SubstanceCompanion>>

    @Query("SELECT * FROM timednote")
    fun getAllTimedNotesFlow(): Flow<List<TimedNote>>

    @Query("SELECT * FROM customunit WHERE isArchived = :isArchived ORDER BY creationDate DESC")
    fun getSortedCustomUnitsFlow(isArchived: Boolean): Flow<List<CustomUnit>>

    @Query("SELECT * FROM timednote")
    suspend fun getAllTimedNotes(): List<TimedNote>

    @Query("SELECT * FROM timednote WHERE experienceId =:experienceId")
    suspend fun getTimedNotes(experienceId: Int): List<TimedNote>
}
