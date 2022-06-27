package com.example.healthassistant.data.room.experiences

import androidx.room.*
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.room.experiences.entities.SubstanceLastUsed
import com.example.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience ORDER BY creationDate DESC")
    fun getExperiencesFlow(): Flow<List<Experience>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getIngestionsSortedDescendingFlow(): Flow<List<Ingestion>>

    @Query("SELECT substanceName, MAX(time) as lastUsed FROM ingestion GROUP BY substanceName ORDER BY lastUsed DESC")
    fun getSubstanceWithLastDateDescendingFlow(): Flow<List<SubstanceLastUsed>>

    @Query("SELECT * FROM ingestion WHERE time > :date ORDER BY time ASC")
    suspend fun getIngestionAfterDate(date: Date): List<Ingestion>

    @Query("SELECT DISTINCT substanceName FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getLastUsedSubstanceNamesFlow(limit: Int): Flow<List<String>>

    @Transaction
    @Query("SELECT DISTINCT e.id, e.title, e.creationDate, e.text FROM experience AS e LEFT JOIN ingestion AS i ON e.id == i.experienceId ORDER BY case when i.time IS NULL then e.creationDate else i.time end DESC")
    fun getSortedExperiencesWithIngestionsFlow(): Flow<List<ExperienceWithIngestions>>

    @Transaction
    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getSortedIngestionsWithSubstanceCompanionsFlow(): Flow<List<IngestionWithCompanion>>

    @Query("SELECT * FROM experience WHERE id =:id")
    suspend fun getExperience(id: Int): Experience?

    @Query("SELECT * FROM experience WHERE id =:id")
    fun getExperienceFlow(id: Int): Flow<Experience?>

    @Query("SELECT * FROM ingestion WHERE id =:id")
    fun getIngestionFlow(id: Int): Flow<Ingestion?>

    @Transaction
    @Query("SELECT * FROM ingestion WHERE id =:id")
    fun getIngestionWithCompanionFlow(id: Int): Flow<IngestionWithCompanion?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(experience: Experience)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(ingestion: Ingestion)

    @Delete
    suspend fun deleteExperience(experience: Experience)

    @Delete
    suspend fun deleteIngestion(ingestion: Ingestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingestion: Ingestion)

    @Transaction
    @Query("SELECT * FROM experience WHERE id = :experienceId")
    fun getExperienceWithIngestionsFlow(experienceId: Int): Flow<ExperienceWithIngestions?>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT 1")
    suspend fun getLastIngestion(substanceName: String): Ingestion?

    @Query("SELECT * FROM substancecompanion")
    fun getSubstanceCompanionFlow(): Flow<List<SubstanceCompanion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(substanceCompanion: SubstanceCompanion)

    @Delete
    suspend fun delete(substanceCompanion: SubstanceCompanion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(substanceCompanion: SubstanceCompanion)
}
