package com.example.healthassistant.data.room.experiences

import androidx.room.*
import com.example.healthassistant.data.room.experiences.entities.Experience
import com.example.healthassistant.data.room.experiences.entities.Ingestion
import com.example.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.example.healthassistant.data.room.experiences.relations.CompanionWithIngestions
import com.example.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.example.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience ORDER BY creationDate DESC")
    fun getExperiencesFlow(): Flow<List<Experience>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getIngestionsSortedDescendingFlow(): Flow<List<Ingestion>>

    @Query(
        "SELECT * FROM ingestion as i" +
                " INNER JOIN (SELECT id, MAX(time) AS maxTime FROM ingestion WHERE time > :date GROUP BY substanceName) as sub" +
                " ON i.id = sub.id AND i.time = sub.maxTime" +
                " ORDER BY time DESC"
    )
    suspend fun getLatestIngestionOfEverySubstanceSinceDate(date: Date): List<Ingestion>

    @Query("SELECT DISTINCT substanceName FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getLastUsedSubstanceNamesFlow(limit: Int): Flow<List<String>>

    @Transaction
    @Query("SELECT DISTINCT e.id, e.title, e.creationDate, e.text, e.sentiment FROM experience AS e LEFT JOIN ingestion AS i ON e.id = i.experienceId ORDER BY case when i.time IS NULL then e.creationDate else i.time end DESC")
    fun getSortedExperiencesWithIngestionsFlow(): Flow<List<ExperienceWithIngestionsAndCompanions>>

    @Transaction
    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getSortedIngestionsWithSubstanceCompanionsFlow(): Flow<List<IngestionWithCompanion>>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT :limit")
    fun getSortedIngestionsFlow(substanceName: String, limit: Int): Flow<List<Ingestion>>

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
    fun getExperienceWithIngestionsFlow(experienceId: Int): Flow<ExperienceWithIngestionsAndCompanions?>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT 1")
    suspend fun getLastIngestion(substanceName: String): Ingestion?

    @Transaction
    @Query("SELECT * FROM substancecompanion")
    fun getSubstanceCompanionWithIngestionsFlow(): Flow<List<CompanionWithIngestions>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(substanceCompanion: SubstanceCompanion)

    @Delete
    suspend fun delete(substanceCompanion: SubstanceCompanion)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(substanceCompanion: SubstanceCompanion)

    @Query("SELECT * FROM substancecompanion WHERE substanceName =:substanceName")
    fun getSubstanceCompanionFlow(substanceName: String): Flow<SubstanceCompanion?>

    @Query("SELECT * FROM substancecompanion")
    fun getAllSubstanceCompanions(): Flow<List<SubstanceCompanion>>
}
