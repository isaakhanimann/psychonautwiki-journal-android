package com.example.healthassistant.data.experiences

import androidx.room.*
import com.example.healthassistant.data.experiences.entities.Experience
import com.example.healthassistant.data.experiences.entities.ExperienceWithIngestions
import com.example.healthassistant.data.experiences.entities.Ingestion
import kotlinx.coroutines.flow.Flow
import java.util.*


@Dao
interface ExperienceDao {

    @Query("SELECT * FROM experience ORDER BY date DESC")
    fun getExperiences(): Flow<List<Experience>>

    @Query("SELECT * FROM ingestion ORDER BY time ASC")
    fun getIngestions(): Flow<List<Ingestion>>

    @Query("SELECT * FROM ingestion WHERE time > :date ORDER BY time ASC")
    suspend fun getIngestionAfterDate(date: Date): List<Ingestion>

    @Query("SELECT DISTINCT substanceName FROM ingestion ORDER BY time DESC LIMIT :limit")
    fun getLastUsedSubstanceNames(limit: Int): Flow<List<String>>

    @Transaction
    @Query("SELECT * FROM experience ORDER BY date DESC")
    fun getExperiencesWithIngestions(): Flow<List<ExperienceWithIngestions>>

    @Query("SELECT * FROM experience ORDER BY date DESC LIMIT :limit")
    suspend fun getLastExperiences(limit: Int): List<Experience>

    @Query("SELECT * FROM experience WHERE id =:id")
    suspend fun getExperienceByID(id: Int): Experience?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(experience: Experience): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(experience: Experience)

    @Query("DELETE FROM ingestion WHERE experienceId = :experienceId")
    suspend fun deleteAllIngestionsFromExperience(experienceId: Int)

    @Transaction
    suspend fun deleteExperienceWithIngestions(experience: Experience) {
        deleteAllIngestionsFromExperience(experienceId = experience.id)
        deleteExperience(experience)
    }

    @Delete
    suspend fun deleteExperience(experience: Experience)

    @Delete
    suspend fun deleteIngestion(ingestion: Ingestion)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ingestion: Ingestion)

    @Transaction
    @Query("SELECT * FROM experience WHERE id = :experienceId")
    fun getExperienceWithIngestions(experienceId: Int): Flow<ExperienceWithIngestions?>

    @Query("SELECT * FROM ingestion WHERE substanceName = :substanceName ORDER BY time DESC LIMIT 1")
    suspend fun getLastIngestion(substanceName: String): Ingestion?
}
