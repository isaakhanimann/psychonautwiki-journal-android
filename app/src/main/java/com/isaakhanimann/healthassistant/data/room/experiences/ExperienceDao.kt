package com.isaakhanimann.healthassistant.data.room.experiences

import androidx.room.*
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Experience
import com.isaakhanimann.healthassistant.data.room.experiences.entities.Ingestion
import com.isaakhanimann.healthassistant.data.room.experiences.entities.SubstanceCompanion
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestions
import com.isaakhanimann.healthassistant.data.room.experiences.relations.ExperienceWithIngestionsAndCompanions
import com.isaakhanimann.healthassistant.data.room.experiences.relations.IngestionWithCompanion
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience ORDER BY creationDate DESC")
    fun getSortedExperiencesFlow(): Flow<List<Experience>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getIngestionsSortedDescendingFlow(): Flow<List<Ingestion>>

    @Query(
        "SELECT * FROM ingestion as i" +
                " INNER JOIN (SELECT id, MAX(time) AS maxTime FROM ingestion WHERE time > :date GROUP BY substanceName) as sub" +
                " ON i.id = sub.id AND i.time = sub.maxTime" +
                " ORDER BY time DESC"
    )
    suspend fun getLatestIngestionOfEverySubstanceSinceDate(date: Date): List<Ingestion>

    @Query("SELECT * FROM ingestion WHERE time > :date")
    suspend fun getIngestionsSinceDate(date: Date): List<Ingestion>

    @Query("SELECT * FROM ingestion")
    suspend fun getAllIngestions(): List<Ingestion>

    @Query("SELECT DISTINCT substanceName FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getLastUsedSubstanceNamesFlow(limit: Int): Flow<List<String>>

    @Transaction
    @Query("SELECT DISTINCT e.id, e.title, e.creationDate, e.text, e.sentiment FROM experience AS e LEFT JOIN ingestion AS i ON e.id = i.experienceId ORDER BY case when i.time IS NULL then e.creationDate else i.time end DESC")
    fun getSortedExperiencesWithIngestionsAndCompanionsFlow(): Flow<List<ExperienceWithIngestionsAndCompanions>>

    @Transaction
    @Query("SELECT DISTINCT e.id, e.title, e.creationDate, e.text, e.sentiment FROM experience AS e LEFT JOIN ingestion AS i ON e.id = i.experienceId ORDER BY case when i.time IS NULL then e.creationDate else i.time end DESC")
    fun getSortedExperiencesWithIngestionsFlow(): Flow<List<ExperienceWithIngestions>>

    @Transaction
    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getSortedIngestionsWithSubstanceCompanionsFlow(): Flow<List<IngestionWithCompanion>>

    @Query("SELECT * FROM ingestion ORDER BY time DESC")
    fun getSortedIngestionsFlow(): Flow<List<Ingestion>>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT :limit")
    fun getSortedIngestionsFlow(substanceName: String, limit: Int): Flow<List<Ingestion>>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC")
    fun getSortedIngestionsFlow(substanceName: String): Flow<List<Ingestion>>

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

    @Transaction
    suspend fun deleteExperienceWithIngestions(experienceWithIngestions: ExperienceWithIngestions) {
        deleteExperience(experience = experienceWithIngestions.experience)
        experienceWithIngestions.ingestions.forEach {
            deleteIngestion(it)
        }
    }

    @Transaction
    suspend fun deleteExperienceWithIngestions(experienceWithIngestionsAndCompanions: ExperienceWithIngestionsAndCompanions) {
        deleteExperience(experience = experienceWithIngestionsAndCompanions.experience)
        experienceWithIngestionsAndCompanions.ingestionsWithCompanions.forEach {
            deleteIngestion(it.ingestion)
        }
    }

    @Delete
    suspend fun deleteIngestion(ingestion: Ingestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingestion: Ingestion)

    @Transaction
    suspend fun insertIngestionExperienceAndCompanion(ingestion: Ingestion, experience: Experience, substanceCompanion: SubstanceCompanion) {
        insert(ingestion)
        insert(experience)
        insert(substanceCompanion)
    }

    @Transaction
    suspend fun insertIngestionAndCompanion(ingestion: Ingestion, substanceCompanion: SubstanceCompanion) {
        insert(ingestion)
        insert(substanceCompanion)
    }

    @Transaction
    @Query("SELECT * FROM experience WHERE id = :experienceId")
    fun getExperienceWithIngestionsAndCompanionsFlow(experienceId: Int): Flow<ExperienceWithIngestionsAndCompanions?>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT 1")
    suspend fun getLastIngestion(substanceName: String): Ingestion?

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
